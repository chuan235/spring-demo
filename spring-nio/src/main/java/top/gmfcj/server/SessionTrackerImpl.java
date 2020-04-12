package top.gmfcj.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gmfcj.util.TimeUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 * @description: session监听器实现
 */
public class SessionTrackerImpl extends Thread implements SessionTracker {

    private static final Logger LOG = LoggerFactory.getLogger(SessionTrackerImpl.class);


    public static class SessionImpl implements Session {
        SessionImpl(long sessionId, int timeout, long expireTime) {
            this.sessionId = sessionId;
            this.timeout = timeout;
            this.tickTime = expireTime;
            isClosing = false;
        }

        final long sessionId;
        final int timeout;
        long tickTime;
        boolean isClosing;
        // owner
        Object owner;

        @Override
        public long getSessionId() {
            return this.sessionId;
        }

        @Override
        public int getTimeout() {
            return this.timeout;
        }

        @Override
        public boolean isClosing() {
            return this.isClosing;
        }
    }

    static class SessionSet {
        HashSet<SessionImpl> sessions = new HashSet<>();
    }

    /**
     * 获取sessionId
     */
    public static long initializeNextSession(long id) {
        long nextSid = 0;
        nextSid = (TimeUtil.currentElapsedTime() << 24) >>> 8;
        nextSid = nextSid | (id << 56);
        return nextSid;
    }

    public SessionExpirer expirer;
    /**
     * sessionId 关联 session
     */
    public HashMap<Long, SessionImpl> sessionsById = new HashMap<>();
    /**
     * expireTime 关联 session集合
     */
    public HashMap<Long, SessionSet> sessionSets = new HashMap<>();
    /**
     * sessionId 关联 session过期时间
     */
    public ConcurrentHashMap<Long, Integer> sessionsWithTimeout;
    public long nextSessionId = 0;
    public long nextExpirationTime;
    /**
     * 校验时间间隔
     */
    public int expirationInterval;
    volatile boolean running = true;
    volatile long currentTime;

    public SessionTrackerImpl(SessionExpirer expirer, ConcurrentHashMap<Long, Integer> sessionsWithTimeout,
                              int tickTime, long sid) {
        this.expirer = expirer;
        this.expirationInterval = tickTime;
        this.sessionsWithTimeout = sessionsWithTimeout;
        nextExpirationTime = roundToInterval(TimeUtil.currentElapsedTime());
        this.nextSessionId = initializeNextSession(sid);
        for (Map.Entry<Long, Integer> e : sessionsWithTimeout.entrySet()) {
            addSession(e.getKey(), e.getValue());
        }
    }

    @Override
    synchronized public void run() {
        try {
            while (running) {
                currentTime = TimeUtil.currentElapsedTime();
                if (nextExpirationTime > currentTime) {
                    this.wait(nextExpirationTime - currentTime);
                    continue;
                }
                SessionSet set;
                set = sessionSets.remove(nextExpirationTime);
                if (set != null) {
                    for (SessionImpl s : set.sessions) {
                        setSessionClosing(s.sessionId);
                        expirer.expire(s);
                    }
                }
                nextExpirationTime += expirationInterval;
            }
        } catch (InterruptedException e) {
            LOG.error("线程中断,stack {}", System.err);
        }
        LOG.info("退出 SessionTrackerImpl 循环!");
    }

    @Override
    public synchronized boolean touchSession(long sessionId, int timeout) {
        SessionImpl s = sessionsById.get(sessionId);
        // session不存在或已关闭
        if (s == null || s.isClosing()) {
            return false;
        }
        long expireTime = roundToInterval(TimeUtil.currentElapsedTime() + timeout);
        if (s.tickTime >= expireTime) {
            // 在有效期内
            return true;
        }
        // 将session放入sessionSet中
        // 检查
        SessionSet set = sessionSets.get(s.tickTime);
        if (set != null) {
            set.sessions.remove(s);
        }
        s.tickTime = expireTime;
        set = sessionSets.get(s.tickTime);
        // 添加
        if (set == null) {
            set = new SessionSet();
            sessionSets.put(expireTime, set);
        }
        set.sessions.add(s);
        return true;
    }

    @Override
    public synchronized void setSessionClosing(long sessionId) {
        SessionImpl s = sessionsById.get(sessionId);
        if (s == null) {
            return;
        }
        s.isClosing = true;
    }

    @Override
    public synchronized void removeSession(long sessionId) {
        SessionImpl s = sessionsById.remove(sessionId);
        sessionsWithTimeout.remove(sessionId);
        if (s != null) {
            SessionSet set = sessionSets.get(s.tickTime);
            // 移除SessionSet中的session
            if (set != null) {
                set.sessions.remove(s);
            }
        }
    }

    @Override
    public void shutdown() {
        LOG.info("session tacker shutting down");
        running = false;
    }

    @Override
    public synchronized long createSession(int sessionTimeout) {
        addSession(nextSessionId, sessionTimeout);
        return nextSessionId++;
    }

    @Override
    public synchronized void addSession(long id, int sessionTimeout) {
        sessionsWithTimeout.put(id, sessionTimeout);
        if (sessionsById.get(id) == null) {
            SessionImpl s = new SessionImpl(id, sessionTimeout, 0);
            sessionsById.put(id, s);

        }
        touchSession(id, sessionTimeout);
    }

    @Override
    public synchronized void checkSession(long sessionId, Object owner) {
        SessionImpl session = sessionsById.get(sessionId);
        if (session == null || session.isClosing()) {
            LOG.error("session 已关闭");
            throw new RuntimeException();
        }
        if (session.owner == null) {
            session.owner = owner;
        } else if (session.owner != owner) {
            LOG.error("session 已移除");
            throw new RuntimeException();
        }
    }

    @Override
    public synchronized void setOwner(long id, Object owner) {
        SessionImpl session = sessionsById.get(id);
        if (session == null || session.isClosing()) {
            LOG.error("session 已失效");
            throw new RuntimeException();
        }
        session.owner = owner;
    }

    /**
     * time对expirationInterval向上取最近的整数倍
     *
     * @param time
     * @return expirationInterval的整数倍时间
     */
    private long roundToInterval(long time) {
        return (time / expirationInterval + 1) * expirationInterval;
    }

}
