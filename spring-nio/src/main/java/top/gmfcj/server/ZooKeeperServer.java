package top.gmfcj.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gmfcj.constant.ZooDefs;
import top.gmfcj.processor.PrepRequestProcessor;
import top.gmfcj.processor.RequestProcessor;
import top.gmfcj.util.ByteBufferInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ZooKeeperServer implements SessionTracker.SessionExpirer {

    protected static final Logger LOG = LoggerFactory.getLogger(ZooKeeperServer.class);

    public static final int DEFAULT_TICK_TIME = 3000;

    protected int tickTime = DEFAULT_TICK_TIME;
    protected int minSessionTimeout = -1;
    protected int maxSessionTimeout = -1;
    protected volatile State state = State.INITIAL;

    protected RequestProcessor firstProcessor;
    protected SessionTracker sessionTracker;

    private ZooKeeperServerShutdownHandler zkShutdownHandler;
    private ServerCnxnFactory serverCnxnFactory;
    private SessionDatabase sessionDatabase;

    public ZooKeeperServer(){
        sessionDatabase = new SessionDatabase();
    }

    @Override
    public void expire(SessionTracker.Session session) {
        long sessionId = session.getSessionId();
        close(sessionId);
    }

    @Override
    public long getServerId() {
        return 0;
    }

    protected enum State {
        INITIAL, RUNNING, SHUTDOWN, ERROR;
    }

    private void close(long sessionId) {
        submitRequest(null, sessionId, ZooDefs.OpCode.closeSession, null);
    }

    private void submitRequest(ServerCnxn cnxn, long sessionId, int type, ByteBuffer bb) {
        Request si = new Request(cnxn, sessionId, type, bb);
        if (firstProcessor == null) {
            // server还没有初始化完成
            synchronized (this) {
                try {
                    while (state == State.INITIAL) {
                        wait(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (firstProcessor == null || state != State.RUNNING) {
                    throw new RuntimeException("Not started");
                }
            }
        }
        try {
            // 调用处理器链来处理请求
            firstProcessor.processRequest(si);
        } catch (Exception e) {
            LOG.error("process request eX", e.getMessage());
        }
    }

    public void submitRequest(Request request) {
        if (firstProcessor == null) {
            // server还没有初始化完成
            synchronized (this) {
                try {
                    while (state == State.INITIAL) {
                        wait(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (firstProcessor == null || state != State.RUNNING) {
                    throw new RuntimeException("Not started");
                }
            }
        }
        try {
            boolean validpacket = Request.isValid(request.type);
            if(validpacket){
                // 调用处理器链来处理请求
                firstProcessor.processRequest(request);
            }
        } catch (Exception e) {
            LOG.error("process request eX", e.getMessage());
        }
    }

    public synchronized void startup() {
        if (sessionTracker == null) {
            createSessionTracker();
        }
        // start session tracker
        startSessionTracker();
        // init request processor
        setupRequestProcessors();
        setState(State.RUNNING);
        notifyAll();
    }

    protected void setupRequestProcessors() {
        // init processor
        firstProcessor = (RequestProcessor) new PrepRequestProcessor(this, null);
    }

    public void processPacket(ServerCnxn cnxn, ByteBuffer incomingBuffer) throws IOException {
        // 获取头信息
        InputStream bais = new ByteBufferInputStream(incomingBuffer);
        BinaryInputArchive bia = BinaryInputArchive.getArchive(bais);
        RequestHeader h = new RequestHeader();
        h.deserialize(bia, "header");
        incomingBuffer = incomingBuffer.slice();
        // 构建request
        Request si = new Request(cnxn, cnxn.getSessionId(), h.getType(), incomingBuffer);
        si.setOwner(ServerCnxn.me);
        // 提交请求
        submitRequest(si);
    }
    public void createSessionTracker() {
        sessionTracker = new SessionTrackerImpl(this, sessionDatabase.getSessionWithTimeOuts(), tickTime, 1);
    }

    public void startSessionTracker(){
        ((SessionTrackerImpl) sessionTracker).start();
    }
    public void shutdown() {
        shutdown(false);
    }

    protected boolean canShutdown() {
        return state == State.RUNNING || state == State.ERROR;
    }

    protected void setState(State state) {
        this.state = state;
    }

    public synchronized void shutdown(boolean fullyShutDown) {
        if (!canShutdown()) {
            LOG.debug("ZooKeeper server is not running, so not proceeding to shutdown!");
            return;
        }
        LOG.info("shutting down");
        setState(State.SHUTDOWN);
        if (sessionTracker != null) {
            sessionTracker.shutdown();
        }
        if (firstProcessor != null) {
            firstProcessor.shutdown();
        }
    }

    public void registerServerShutdownHandler(ZooKeeperServerShutdownHandler zkShutdownHandler) {
        this.zkShutdownHandler = zkShutdownHandler;
    }

    public ZooKeeperServerShutdownHandler getZkShutdownHandler() {
        return zkShutdownHandler;
    }

    public void setServerCnxnFactory(ServerCnxnFactory factory) {
        serverCnxnFactory = factory;
    }

    public ServerCnxnFactory getServerCnxnFactory() {
        return serverCnxnFactory;
    }

    public SessionTracker getSessionTracker() {
        return sessionTracker;
    }
}
