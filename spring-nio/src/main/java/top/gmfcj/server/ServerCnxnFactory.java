package top.gmfcj.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class ServerCnxnFactory {

    public static final String ZOOKEEPER_SERVER_CNXN_FACTORY = "zookeeper.serverCnxnFactory";

    public interface PacketProcessor {
        public void processPacket(ByteBuffer packet, ServerCnxn src);
    }

    private static final Logger LOG = LoggerFactory.getLogger(ServerCnxnFactory.class);

    // sessionMap is used to speed up closeSession()
    protected final ConcurrentMap<Long, ServerCnxn> sessionMap =
            new ConcurrentHashMap<Long, ServerCnxn>();

    /**
     * The buffer will cause the connection to be close when we do a send.
     */
    static final ByteBuffer closeConn = ByteBuffer.allocate(0);

    public abstract int getLocalPort();

    public abstract Iterable<ServerCnxn> getConnections();

    public int getNumAliveConnections() {
        synchronized (cnxns) {
            return cnxns.size();
        }
    }

    ZooKeeperServer getZooKeeperServer() {
        return zkServer;
    }

    public abstract void closeSession(long sessionId);

    public abstract void configure(InetSocketAddress addr, int maxClientCnxns) throws IOException;

    /**
     * Maximum number of connections allowed from particular host (ip)
     */
    public abstract int getMaxClientCnxnsPerHost();

    /**
     * Maximum number of connections allowed from particular host (ip)
     */
    public abstract void setMaxClientCnxnsPerHost(int max);

    public abstract void startup(ZooKeeperServer zkServer) throws IOException, InterruptedException;

    public abstract void join() throws InterruptedException;

    public abstract void shutdown();

    public abstract void start();

    protected ZooKeeperServer zkServer;

    final public void setZooKeeperServer(ZooKeeperServer server) {
        this.zkServer = server;
        if (server != null) {
            server.setServerCnxnFactory(this);
        }
    }

    public abstract void closeAll();

    public static ServerCnxnFactory createFactory() throws IOException {
        return new NIOServerCnxnFactory();
    }

    static public ServerCnxnFactory createFactory(int clientPort,
                                                  int maxClientCnxns) throws IOException {
        return createFactory(new InetSocketAddress(clientPort), maxClientCnxns);
    }

    static public ServerCnxnFactory createFactory(InetSocketAddress addr,
                                                  int maxClientCnxns) throws IOException {
        ServerCnxnFactory factory = createFactory();
        factory.configure(addr, maxClientCnxns);
        return factory;
    }

    public abstract InetSocketAddress getLocalAddress();


    protected final HashSet<ServerCnxn> cnxns = new HashSet<ServerCnxn>();


    public void addSession(long sessionId, ServerCnxn cnxn) {
        sessionMap.put(sessionId, cnxn);
    }


}
