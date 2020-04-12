package top.gmfcj.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * start class
 */
public class QuorumPeerMain {

    private static final Logger LOG = LoggerFactory.getLogger(QuorumPeerMain.class);

    public static void main(String[] args) throws Exception {
        QuorumPeerMain main = new QuorumPeerMain();
        main.initializeAndRun(args);
    }

    private ServerCnxnFactory cnxnFactory;

    protected void initializeAndRun(String[] args) throws Exception {
        // 解析配置文件
        ServerConfig config = new ServerConfig();
        if (args.length == 1) {
            config.parse(args[0]);
        } else {
            config.parse(args);
        }
        runFromConfig(config);
    }

    public void runFromConfig(ServerConfig config) throws IOException, InterruptedException {
        LOG.info("Starting server");
        try {
            final ZooKeeperServer zkServer = new ZooKeeperServer();
            // 防止服务器出错
            final CountDownLatch shutdownLatch = new CountDownLatch(1);
            // 异常回调
            zkServer.registerServerShutdownHandler(new ZooKeeperServerShutdownHandler(shutdownLatch));
            cnxnFactory = ServerCnxnFactory.createFactory();
            // 初始化cnxnFactory
            cnxnFactory.configure(config.getServerPortAddress(), config.getMaxClientCnxns());
            cnxnFactory.startup(zkServer);
            shutdownLatch.await();
            shutdown();
            cnxnFactory.join();
            if (zkServer.canShutdown()) {
                zkServer.shutdown(true);
            }
        }catch (InterruptedException e) {
            LOG.warn("Server interrupted", e);
        }
    }

    protected void shutdown() {
        if (cnxnFactory != null) {
            cnxnFactory.shutdown();
        }
    }
}
