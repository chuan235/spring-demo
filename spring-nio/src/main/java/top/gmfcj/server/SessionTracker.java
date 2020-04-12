package top.gmfcj.server;

/**
 * 服务器session监听器
 */
public interface SessionTracker {

    public static interface Session {
        // session唯一标识
        long getSessionId();
        // session过期时间
        int getTimeout();
        // session状态
        boolean isClosing();
    }

    public static interface SessionExpirer {
        // 手动关闭session
        void expire(Session session);
        // 返回当前服务器的id
        long getServerId();
    }

    /**
     * 指定session过期时间创建session
     * @param sessionTimeout
     * @return
     */
    long createSession(int sessionTimeout);

    /**
     * 添加session
     * @param id
     * @param to
     */
    void addSession(long id, int to);

    /**
     * 检查session
     * @param sessionId
     * @param sessionTimeout
     * @return 如果session不在活动，返回false
     */
    boolean touchSession(long sessionId, int sessionTimeout);

    /**
     * 将session标记为关闭状态
     * @param sessionId
     */
    void setSessionClosing(long sessionId);

    /**
     * 关闭session监听器
     */
    void shutdown();

    /**
     * 从session缓存中移除session
     * @param sessionId
     */
    void removeSession(long sessionId);

    /**
     * 检查session是否存在或者过期
     * @param sessionId
     * @param owner
     */
    void checkSession(long sessionId, Object owner);

    /**
     * 设置session的owner
     * @param id
     * @param owner
     */
    void setOwner(long id, Object owner);

}
