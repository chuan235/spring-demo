package top.springnio;

import io.netty.channel.Channel;

public class NettySession {

    // Session的唯一标识
    private String sessionId;
    // 和Session相关的channel,通过它向客户端回送数据
    private Channel channel = null;
    // 上次通信时间
    private long lastTimeStamp = 0l;

    // 快速构建新的Session
    public static NettySession buildSession(Channel channel) {
        NettySession session = new NettySession();
        session.setChannel(channel);
        // 使用netty生成的类似UUID的字符串,来标识一个session
        session.setSessionId(channel.id().asLongText());
        session.setLastTimeStamp(System.currentTimeMillis());
        return session;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public long getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(long lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }
}
