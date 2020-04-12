package top.gmfcj.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class NIOServerCnxnFactory extends ServerCnxnFactory implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(NIOServerCnxnFactory.class);

    static {
        try {
            Selector.open().close();
        } catch (IOException ie) {
            LOG.error("Selector failed to open", ie);
        }
    }

    ServerSocketChannel serverSocketChannel;

    final Selector selector = Selector.open();

    final ByteBuffer directBuffer = ByteBuffer.allocateDirect(64 * 1024);

    final HashMap<InetAddress, Set<NIOServerCnxn>> ipMap = new HashMap<>();

    int maxClientCnxns = 60;

    public NIOServerCnxnFactory() throws IOException {
    }

    Thread thread;

    @Override
    public void configure(InetSocketAddress addr, int maxcc) throws IOException {
        // 创建一个 NIOServerCxn.Factory 的线程,其实就是当前对象   NIOServerCnxnFactory
        thread = new Thread(this, "NIOServerCxn.Factory:" + addr);
        // 守护线程
        thread.setDaemon(true);
        maxClientCnxns = maxcc;
        // 打开一个NIO通道
        this.serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().setReuseAddress(true);
        LOG.info("binding to port " + addr);
        serverSocketChannel.socket().bind(addr);
        // 将Channel和Selector配合使用
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public int getMaxClientCnxnsPerHost() {
        return maxClientCnxns;
    }

    @Override
    public void setMaxClientCnxnsPerHost(int max) {
        maxClientCnxns = max;
    }

    @Override
    public void start() {
        if (thread.getState() == Thread.State.NEW) {
            thread.start();
        }
    }

    @Override
    public void startup(ZooKeeperServer zks) throws IOException, InterruptedException {
        // 启动NIOServerCnxnFactory 当前对象这个线程
        start();
        // 设置属性
        setZooKeeperServer(zks);
        // 启动Zkserver服务
        zks.startup();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) serverSocketChannel.socket().getLocalSocketAddress();
    }

    @Override
    public int getLocalPort() {
        return serverSocketChannel.socket().getLocalPort();
    }

    private void addCnxn(NIOServerCnxn cnxn) {
        synchronized (cnxns) {
            cnxns.add(cnxn);
            synchronized (ipMap) {
                InetAddress addr = cnxn.socketChannel.socket().getInetAddress();
                Set<NIOServerCnxn> s = ipMap.get(addr);
                if (s == null) {
                    s = new HashSet<>(2);
                    s.add(cnxn);
                    ipMap.put(addr, s);
                } else {
                    s.add(cnxn);
                }
            }
        }
    }

    public void removeCnxn(NIOServerCnxn cnxn) {
        synchronized (cnxns) {
            // 移除session
            long sessionId = cnxn.getSessionId();
            if (sessionId != 0) {
                sessionMap.remove(sessionId);
            }
            // 移除serverCnxn
            if (!cnxns.remove(cnxn)) {
                return;
            }
            // 移除ip client
            synchronized (ipMap) {
                Set<NIOServerCnxn> s = ipMap.get(cnxn.getSocketAddress());
                s.remove(cnxn);
            }
        }
    }

    protected NIOServerCnxn createConnection(SocketChannel sock, SelectionKey sk) throws IOException {
        return new NIOServerCnxn(zkServer, sock, sk, this);
    }

    private int getClientCnxnCount(InetAddress cl) {
        // The ipMap lock covers both the map, and its contents
        // (that is, the cnxn sets shouldn't be modified outside of
        // this lock)
        synchronized (ipMap) {
            Set<NIOServerCnxn> s = ipMap.get(cl);
            if (s == null) return 0;
            return s.size();
        }
    }

    @Override
    public void run() {
        while (!serverSocketChannel.socket().isClosed()) {
            try {
                selector.select(1000);
                Set<SelectionKey> selected;
                synchronized (this) {
                    selected = selector.selectedKeys();
                }
                ArrayList<SelectionKey> selectedList = new ArrayList<SelectionKey>(selected);
                for (SelectionKey k : selectedList) {
                    if ((k.readyOps() & SelectionKey.OP_ACCEPT) != 0) {
                        // 如果操作类型是ACCEPT，进入这里
                        // ServerSocketChannel 只支持OP_ACCEPT
                        // 调用accept方法生成服务端的SocketChannel,它支持OP_READ，OP_WRITE
                        SocketChannel sc = ((ServerSocketChannel) k.channel()).accept();
                        InetAddress ia = sc.socket().getInetAddress();
                        int cnxncount = getClientCnxnCount(ia);
                        // 检查客户端的数量
                        if (maxClientCnxns > 0 && cnxncount >= maxClientCnxns) {
                            LOG.warn("Too many connections from " + ia + " - max is " + maxClientCnxns);
                            sc.close();
                        } else {
                            LOG.info("Accepted socket connection from " + sc.socket().getRemoteSocketAddress());
                            // 和Selector关联起来，将SocketChannel注册到selector上
                            sc.configureBlocking(false);
                            SelectionKey sk = sc.register(selector, SelectionKey.OP_READ);
                            // 为每一个channel和SelectionKey创建ServerCnxn
                            NIOServerCnxn cnxn = createConnection(sc, sk);
                            // 关联selectionKey与ServerCnxn
                            sk.attach(cnxn);
                            // 将cnxn加入到cnxns
                            addCnxn(cnxn);
                        }
                    } else if ((k.readyOps() & (SelectionKey.OP_READ | SelectionKey.OP_WRITE)) != 0) {
                        // 可读或者可写的操作类型
                        // 在上面连接的时候就为每一个SelectionKey关联了一个NIOServerCnxn对象，这里只是将关联的上下文对象拿出来
                        // 读取请求
                        NIOServerCnxn c = (NIOServerCnxn) k.attachment();
                        //c.initialized = true;
                        c.doIO(k);
                    } else {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Unexpected ops in select " + k.readyOps());
                        }
                    }
                }
                selected.clear();
            } catch (RuntimeException e) {
                LOG.warn("Ignoring unexpected runtime exception", e);
            } catch (Exception e) {
                LOG.warn("Ignoring exception", e);
            }
        }
        closeAll();
        LOG.info("NIOServerCnxn factory exited run method");
    }

    @Override
    synchronized public void closeAll() {
        selector.wakeup();
        HashSet<NIOServerCnxn> cnxns;
        synchronized (this.cnxns) {
            cnxns = (HashSet<NIOServerCnxn>) this.cnxns.clone();
        }
        // got to clear all the connections that we have in the selector
        for (NIOServerCnxn cnxn : cnxns) {
            try {
                // don't hold this.cnxns lock as deadlock may occur
                cnxn.close();
            } catch (Exception e) {
                LOG.warn("Ignoring exception closing cnxn sessionid 0x"
                        + Long.toHexString(cnxn.sessionId), e);
            }
        }
    }

    public void shutdown() {
        try {
            serverSocketChannel.close();
            closeAll();
            thread.interrupt();
            thread.join();
        } catch (Exception e) {
            LOG.warn("Ignoring unexpected exception during shutdown", e);
        }
        try {
            selector.close();
        } catch (IOException e) {
            LOG.warn("Selector closing exception", e);
        }
        if (zkServer != null) {
            zkServer.shutdown();
        }
    }

    @Override
    public synchronized void closeSession(long sessionId) {
        selector.wakeup();
        closeSessionWithoutWakeup(sessionId);
    }

    private void closeSessionWithoutWakeup(long sessionId) {
        NIOServerCnxn cnxn = (NIOServerCnxn) sessionMap.remove(sessionId);
        if (cnxn != null) {
            try {
                cnxn.close();
            } catch (Exception e) {
                LOG.warn("exception during session close", e);
            }
        }
    }

    @Override
    public void join() throws InterruptedException {
        thread.join();
    }

    @Override
    public Iterable<ServerCnxn> getConnections() {
        return cnxns;
    }
}
