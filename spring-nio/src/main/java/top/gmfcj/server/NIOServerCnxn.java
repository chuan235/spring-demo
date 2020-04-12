package top.gmfcj.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gmfcj.constant.Constants;
import top.gmfcj.util.ByteBufferInputStream;
import top.gmfcj.util.CommonUtil;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingQueue;


public class NIOServerCnxn extends ServerCnxn {

    static final Logger LOG = LoggerFactory.getLogger(NIOServerCnxn.class);

    private static final String ZK_NOT_SERVING = "服务无法处理当前请求";

    NIOServerCnxnFactory factory;

    final SocketChannel socketChannel;

    protected final SelectionKey selectionKey;

    boolean initialized;

    ByteBuffer lenBuffer = ByteBuffer.allocate(4);

    ByteBuffer incomingBuffer = lenBuffer;

    LinkedBlockingQueue<ByteBuffer> outgoingBuffers = new LinkedBlockingQueue<ByteBuffer>();

    int sessionTimeout;

    protected final ZooKeeperServer zkServer;

    int outstandingRequests;

    long sessionId;

    static long nextSessionId = 1;

    int outstandingLimit = 1;

    // 解码buffer
    private Charset charset = Charset.forName("ASCII");

    public NIOServerCnxn(ZooKeeperServer zk, SocketChannel sock, SelectionKey sk, NIOServerCnxnFactory factory) throws IOException {
        this.zkServer = zk;
        this.socketChannel = sock;
        this.selectionKey = sk;
        this.factory = factory;
        sock.socket().setTcpNoDelay(true);
        sock.socket().setSoLinger(false, -1);
        sk.interestOps(SelectionKey.OP_READ);
    }

    @Override
    public InetSocketAddress getRemoteSocketAddress() {
        if (socketChannel.isOpen() == false) {
            return null;
        }
        return (InetSocketAddress) socketChannel.socket().getRemoteSocketAddress();
    }

    @Override
    public InetAddress getSocketAddress() {
        if (socketChannel == null) {
            return null;
        }
        return socketChannel.socket().getInetAddress();
    }

    @Override
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    @Override
    public long getSessionId() {
        return sessionId;
    }

    @Override
    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
        this.factory.addSession(sessionId, this);
    }

    @Override
    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    @Override
    public int getInterestOps() {
        return selectionKey.isValid() ? selectionKey.interestOps() : 0;
    }

    @Override
    public void sendBuffer(ByteBuffer bb) {
        if (bb != ServerCnxnFactory.closeConn) {
            // 如果buffer是空，直接返回
            if (bb.remaining() == 0) {
                return;
            }
            if (selectionKey.isValid() && ((selectionKey.interestOps() & SelectionKey.OP_WRITE) == 0)) {
                try {
                    socketChannel.write(bb);
                } catch (IOException e) {
                    LOG.error("socketChannel write error", e);
                }
            }
        }
        synchronized (this.factory) {
            selectionKey.selector().wakeup();
            LOG.info("添加一个buffer 到 outgoingBuffers, selectionKey " + selectionKey);
            outgoingBuffers.add(bb);

            if (selectionKey.isValid()) {
                selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
            }
        }
    }

    @Override
    public void close() {
        factory.removeCnxn(this);
        closeSock();
        if (selectionKey != null) {
            selectionKey.cancel();
        }
    }

    @Override
    public void sendCloseSession() {
        sendBuffer(ServerCnxnFactory.closeConn);
    }


    public void doIO(SelectionKey key) throws InterruptedException {
        try {
            if (isSocketOpen() == false) {
                LOG.warn("打开 session:0x{} 的socket通道失败", Long.toHexString(sessionId));
                return;
            }
            // 读就绪
            if (key.isReadable()) {
                // 客户端发送的数据都在 incomingBuffer 中
                int rc = socketChannel.read(incomingBuffer);
                if (rc < 0) {
                    LOG.error("无法从客户端sessionId: 0x{} 读取数据", Long.toHexString(sessionId));
                    throw new RuntimeException("无法从客户端sessionId: 0x" + Long.toHexString(sessionId) + " 读取数据");
                }
//                incomingBuffer.flip();
//                String requestMsg = String.valueOf(charset.decode(incomingBuffer).array());
                if (incomingBuffer.remaining() == 0) {
                    boolean isPayload = true;
                    if (incomingBuffer == lenBuffer) {
                        incomingBuffer.flip();
                        isPayload = readLength(key);
                        incomingBuffer.clear();
                    }
                    if (isPayload) {
                        // 处理非command请求
                        readPayload();
                    } else {
                        return;
                    }
                }
            }
            if (key.isWritable()) {
                if (outgoingBuffers.size() > 0) {
                    ByteBuffer directBuffer = factory.directBuffer;
                    directBuffer.clear();
                    for (ByteBuffer b : outgoingBuffers) {
                        if (directBuffer.remaining() < b.remaining()) {
                            b = (ByteBuffer) b.slice().limit(directBuffer.remaining());
                        }
                        int p = b.position();
                        directBuffer.put(b);
                        b.position(p);
                        if (directBuffer.remaining() == 0) {
                            break;
                        }
                    }
                    directBuffer.flip();
                    int sent = socketChannel.write(directBuffer);
                    ByteBuffer bb;
                    // 移除已发送的buffer
                    while (outgoingBuffers.size() > 0) {
                        bb = outgoingBuffers.peek();
                        if (bb == ServerCnxnFactory.closeConn) {
                            throw new RuntimeException("close requested");
                        }
                        int left = bb.remaining() - sent;
                        if (left > 0) {
                            bb.position(bb.position() + sent);
                            break;
                        }
                        sent -= bb.remaining();
                        outgoingBuffers.remove();
                    }
                }
                synchronized (this.factory) {
                    if (outgoingBuffers.size() == 0) {
                        if (!initialized && (selectionKey.interestOps() & SelectionKey.OP_READ) == 0) {
                            throw new RuntimeException("请求已关闭");
                        }
                        selectionKey.interestOps(selectionKey.interestOps() & (~SelectionKey.OP_WRITE));
                    } else {
                        selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            close();
        }
    }

    private void closeSock() {
        if (socketChannel.isOpen() == false) {
            return;
        }
        try {
            socketChannel.socket().shutdownOutput();
        } catch (IOException e) {
            LOG.debug("忽略 socket shutdown output exception", e);
        }
        try {
            socketChannel.socket().shutdownInput();
        } catch (IOException e) {
            LOG.debug("忽略 socket shutdown input exception", e);

        }
        try {
            socketChannel.socket().close();
        } catch (IOException e) {
            LOG.debug("忽略 socket close exception", e);

        }
        try {
            socketChannel.close();
        } catch (IOException e) {
            LOG.debug("忽略 socketchannel close exception", e);
        }
    }

    public boolean isZKServerRunning() {
        return zkServer != null;//&& zkServer.isRunning();
    }

    protected boolean isSocketOpen() {
        return socketChannel.isOpen();
    }

    private boolean readLength(SelectionKey k) throws IOException {
        int len = lenBuffer.getInt();
        if (!initialized && checkFourLetterWord(selectionKey, len)) {
            return false;
        }
        if (len < 0 || len > Constants.MAX_BUFFER) {
            throw new IOException("数据长度超过限制 length " + len);
        }
        if (!isZKServerRunning()) {
            throw new IOException("Server not running");
        }
        incomingBuffer = ByteBuffer.allocate(6);
        return true;
    }

    private boolean checkFourLetterWord(SelectionKey selectionKey, int len) {
        if (!ServerCnxn.isKnown(len)) {
            return false;
        }
        if (selectionKey != null) {
            selectionKey.cancel();
        }
        String cmd = ServerCnxn.getCommandString(len);
        LOG.info("processing cmd >>> {} command from {}", cmd, socketChannel.socket().getRemoteSocketAddress());
        // 开启线程处理cmd
        if (len == openCmd) {
            OpenCommand openCommand = new OpenCommand();
            openCommand.start();
            return true;
        } else if (len == stopCmd) {
            StopCommand stopCommand = new StopCommand();
            stopCommand.start();
            return true;
        }

        return false;
    }

    private void readPayload() throws IOException {
        if (incomingBuffer.remaining() != 0) {
            // 能读取到数据 no-blocking 直接读
            int rc = socketChannel.read(incomingBuffer);
            if (rc < 0) {
                throw new RuntimeException("从客户端读取数据失败 sessionId = 0x" + Long.toHexString(sessionId));
            }
        }
        if (incomingBuffer.remaining() == 0) {
            // 接收packet准备
            incomingBuffer.flip();
            if (!initialized) {
                // 如果与客户段的连接还没有初始化,再这里处理连接请求
                readConnectRequest();
            } else {
                // 读取请求
                readRequest();
            }
            lenBuffer.clear();
            incomingBuffer = lenBuffer;
        }
    }

    private void readConnectRequest() throws IOException {
        if (!isZKServerRunning()) {
            throw new IOException("Server not running");
        }
        // todo 开始调用请求处理器链
        //zkServer.processConnectRequest(this, incomingBuffer);
        initialized = true;
    }

    private void readRequest() throws IOException {
        // todo 服务器开始处理buffer
        zkServer.processPacket(this, incomingBuffer);
    }

    private class OpenCommand extends CommandThread {
        @Override
        public void commandRun() throws IOException {
            if (!isZKServerRunning()) {
                LOG.error(ZK_NOT_SERVING);
            } else {
                incomingBuffer = ByteBuffer.allocate(5);
                LOG.debug("processing open command");
                int len = socketChannel.read(incomingBuffer);
                if (len < 0) {
                    throw new RuntimeException("从客户端读取数据失败 sessionId = 0x" + Long.toHexString(sessionId));
                }
                if (incomingBuffer.position() > 0) {
                    String backId = CommonUtil.buildString(incomingBuffer, Charset.forName("ASCII"));
                    LOG.info("open back ... backId = {}", backId);
                    sendResponse("success");
                }

            }
        }
    }

    @Override
    public void sendResponse(String reply) throws IOException {
        byte[] bytes = reply.getBytes();
        ByteBuffer sendBuffer = ByteBuffer.wrap(bytes);
        sendBuffer(sendBuffer);
    }

    private class StopCommand extends CommandThread {

        @Override
        public void commandRun() throws IOException {
            if (!isZKServerRunning()) {
                LOG.error(ZK_NOT_SERVING);
            } else {
                LOG.debug("processing stop command");
            }
        }
    }

    private abstract class CommandThread extends Thread {

        public void run() {
            try {
                commandRun();
            } catch (IOException ie) {
                LOG.error("Error in running command ", ie);
            } finally {
                try {
                    close();
                } catch (Exception e) {
                    LOG.error("Error closing a command socket ", e);
                }
            }
        }

        public abstract void commandRun() throws IOException;
    }

    private class SendBufferWriter extends Writer {

        private StringBuffer sb = new StringBuffer();

        private void checkFlush(boolean force) {
            if ((force && sb.length() > 0) || sb.length() > 2048) {
                sendBufferSync(ByteBuffer.wrap(sb.toString().getBytes()));
                // clear our internal buffer
                sb.setLength(0);
            }
        }

        @Override
        public void close() throws IOException {
            if (sb == null) return;
            checkFlush(true);
            sb = null; // clear out the ref to ensure no reuse
        }

        @Override
        public void flush() throws IOException {
            checkFlush(true);
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            sb.append(cbuf, off, len);
            checkFlush(false);
        }
    }

    void sendBufferSync(ByteBuffer bb) {
        try {
            socketChannel.configureBlocking(true);
            if (bb != ServerCnxnFactory.closeConn) {
                if (socketChannel.isOpen()) {
                    socketChannel.write(bb);
                }
            }
        } catch (IOException ie) {
            LOG.error("Error sending data synchronously ", ie);
        }
    }
}
