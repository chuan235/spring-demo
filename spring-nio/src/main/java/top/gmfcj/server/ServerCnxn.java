package top.gmfcj.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 一个ServerCnxn代表一个客户端连接
 */
public abstract class ServerCnxn {

    final public static Object me = new Object();
    private static final Logger LOG = LoggerFactory.getLogger(ServerCnxn.class);
    final static Map<Integer, String> cmd2String = new HashMap<>();

    public abstract InetAddress getSocketAddress();

    public abstract InetSocketAddress getRemoteSocketAddress();

    public abstract long getSessionId();

    public abstract int getSessionTimeout();

    public abstract void setSessionId(long sessionId);

    public abstract void sendBuffer(ByteBuffer closeConn);

    public abstract void setSessionTimeout(int sessionTimeout);

    public abstract void close();

    public abstract void sendResponse(String reply)  throws IOException;

    public abstract void sendCloseSession();

    public abstract int getInterestOps();


    public static boolean isKnown(int command) {
        return cmd2String.containsKey(command);
    }
    public static String getCommandString(int command) {
        return cmd2String.get(command);
    }

    protected final static int openCmd = ByteBuffer.wrap("open".getBytes()).getInt();
    protected final static int stopCmd = ByteBuffer.wrap("stop".getBytes()).getInt();

    static {
        cmd2String.put(openCmd, "open");
        cmd2String.put(stopCmd, "stop");

    }
}
