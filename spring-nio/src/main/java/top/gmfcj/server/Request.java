package top.gmfcj.server;

import top.gmfcj.constant.ZooDefs.OpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public class Request {
    private static final Logger LOG = LoggerFactory.getLogger(Request.class);
    public static final Request REQUEST_OF_DEATH = new Request(null, 0, 0, null);

    public final long sessionId;
    public final int type;
    public final ByteBuffer request;
    public final ServerCnxn cnxn;

    private Object owner;

    public Request(ServerCnxn cnxn, long sessionId, int type, ByteBuffer bb) {
        this.cnxn = cnxn;
        this.sessionId = sessionId;
        this.type = type;
        this.request = bb;
    }

    public static boolean isValid(int type) {
        switch (type) {
            case OpCode.notification:
                return false;
            case OpCode.open:
            case OpCode.stop:
            case OpCode.ping:
            case OpCode.createSession:
            case OpCode.closeSession:
            case OpCode.error:
                return true;
            default:
                return false;
        }
    }

    public static String op2String(int type) {
        switch (type) {
            case OpCode.notification:
                return "notification";
            case OpCode.open:
                return "open";
            case OpCode.stop:
                return "stop";
            case OpCode.ping:
                return "ping";
            case OpCode.createSession:
                return "createSession";
            case OpCode.closeSession:
                return "createSession";
            case OpCode.error:
                return "error";
            default:
                return "unknown" + type;
        }
    }

    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }
}
