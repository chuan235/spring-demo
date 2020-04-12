package top.gmfcj.server;

import java.util.concurrent.ConcurrentHashMap;

public class SessionDatabase {

    protected ConcurrentHashMap<Long, Integer> sessionsWithTimeouts;
    private volatile boolean initialized = false;

    public SessionDatabase(){
        sessionsWithTimeouts = new ConcurrentHashMap<>();
    }

    public void clear() {
        sessionsWithTimeouts.clear();
        initialized = false;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public ConcurrentHashMap<Long, Integer> getSessionWithTimeOuts() {
        return sessionsWithTimeouts;
    }

}
