package top.gmfcj.constant;

public class ZooDefs {

    public interface OpCode {
        public final int notification = 0;
        public final int open = 1;
        public final int stop = 2;
        public final int ping = 3;
        public final int createSession = -10;
        public final int closeSession = -11;
        public final int error = -1;
    }
}
