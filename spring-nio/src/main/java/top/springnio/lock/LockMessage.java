package top.springnio.lock;

public class LockMessage {

    private LockHeader header;
    private String body;

    public LockMessage(LockHeader header, String body) {
        this.header = header;
        this.body = body;
    }

    public LockHeader getHead() {
        return header;
    }

    public void setHead(LockHeader header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
