package top.gmfcj.server;

import java.util.concurrent.CountDownLatch;
import top.gmfcj.server.ZooKeeperServer.State;


class ZooKeeperServerShutdownHandler {

    private final CountDownLatch shutdownLatch;

    ZooKeeperServerShutdownHandler(CountDownLatch shutdownLatch) {
        this.shutdownLatch = shutdownLatch;
    }

    void handle(State state) {
        if (state == State.ERROR || state == State.SHUTDOWN) {
            shutdownLatch.countDown();
        }
    }
}
