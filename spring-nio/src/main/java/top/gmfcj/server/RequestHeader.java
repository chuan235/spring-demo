package top.gmfcj.server;

import top.gmfcj.util.InputArchive;
import top.gmfcj.util.OutputArchive;
import top.gmfcj.util.Record;

public class RequestHeader implements Record {
    private int xid;
    private int type;

    public RequestHeader() {
    }

    public RequestHeader(int xid, int type) {
        this.xid = xid;
        this.type = type;
    }

    public void serialize(OutputArchive outputArchive, String tag) throws java.io.IOException {
        outputArchive.startRecord(this, tag);
        outputArchive.writeInt(xid, "xid");
        outputArchive.writeInt(type, "type");
        outputArchive.endRecord(this, tag);
    }

    public void deserialize(InputArchive inputArchive, String tag) throws java.io.IOException {
        inputArchive.startRecord(tag);
        xid = inputArchive.readInt("xid");
        type = inputArchive.readInt("type");
        inputArchive.endRecord(tag);
    }


    public void write(java.io.DataOutput out) throws java.io.IOException {
        BinaryOutputArchive archive = new BinaryOutputArchive(out);
        serialize(archive, "");
    }

    public void readFields(java.io.DataInput in) throws java.io.IOException {
        BinaryInputArchive archive = new BinaryInputArchive(in);
        deserialize(archive, "");
    }

    public int compareTo(Object peer_) throws ClassCastException {
        if (!(peer_ instanceof RequestHeader)) {
            throw new ClassCastException("Comparing different types of records.");
        }
        RequestHeader peer = (RequestHeader) peer_;
        int ret = 0;
        ret = (xid == peer.xid) ? 0 : ((xid < peer.xid) ? -1 : 1);
        if (ret != 0) return ret;
        ret = (type == peer.type) ? 0 : ((type < peer.type) ? -1 : 1);
        if (ret != 0) return ret;
        return ret;
    }

    public boolean equals(Object o) {
        if (!(o instanceof RequestHeader)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        RequestHeader peer = (RequestHeader) o;
        boolean ret = false;
        ret = (xid == peer.xid);
        if (!ret) return ret;
        ret = (type == peer.type);
        if (!ret) return ret;
        return ret;
    }

    public int hashCode() {
        int result = 17;
        int ret = xid;
        result = 37 * result + ret;
        ret = type;
        result = 37 * result + ret;
        return result;
    }

    public int getXid() {
        return xid;
    }

    public void setXid(int xid) {
        this.xid = xid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
