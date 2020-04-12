package top.gmfcj.processor;

import top.gmfcj.server.Request;

public interface RequestProcessor {

    public static class RequestProcessorException extends Exception {
        public RequestProcessorException(String msg, Throwable t) {
            super(msg, t);
        }
    }

    void processRequest(Request request) throws RequestProcessorException;

    void shutdown();
}
