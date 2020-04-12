package top.gmfcj.processor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gmfcj.constant.ZooDefs.OpCode;
import top.gmfcj.server.Request;
import top.gmfcj.server.ZooKeeperServer;

import java.util.concurrent.LinkedBlockingQueue;

public class PrepRequestProcessor extends Thread implements RequestProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(PrepRequestProcessor.class);

    LinkedBlockingQueue<Request> submittedRequests = new LinkedBlockingQueue<>();

    RequestProcessor nextProcessor;

    ZooKeeperServer zooKeeperServer;

    public PrepRequestProcessor(ZooKeeperServer zks, RequestProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
        this.zooKeeperServer = zks;
    }


    @Override
    public void run() {
        try {
            while (true) {
                // 从阻塞队列中取出request
                Request request = submittedRequests.take();
                // 处理请求
                pRequest(request);
            }
        } catch (Exception e) {
            LOG.info("PrepRequestProcessor exec exception! {}", e.getMessage());
        }
        LOG.info("PrepRequestProcessor exited loop!");
    }

    protected void pRequest(Request request) throws RequestProcessorException {
        LOG.info("Prep>>> type = " + request.type + " id = 0x" + Long.toHexString(request.sessionId));
        switch (request.type) {
            case OpCode.open:
                System.out.println("处理开启锁");
                break;
            case OpCode.stop:
                System.out.println("处理关闭锁");
                break;
            case OpCode.ping:
                zooKeeperServer.getSessionTracker().checkSession(request.sessionId, request.getOwner());
                break;
            case OpCode.createSession:
            case OpCode.closeSession:
                System.out.println("处理session");
                break;
        }
    }

    public void processRequest(Request request) {
         LOG.info("add prep >>> size ="+ submittedRequests.size());
        submittedRequests.add(request);
    }

    public void shutdown() {
        LOG.info("Shutting down");
        submittedRequests.clear();
        nextProcessor.shutdown();
    }
}
