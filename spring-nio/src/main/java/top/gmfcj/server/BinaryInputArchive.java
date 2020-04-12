package top.gmfcj.server;

import top.gmfcj.constant.Constants;
import top.gmfcj.util.InputArchive;
import top.gmfcj.util.Record;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BinaryInputArchive implements InputArchive {
    static public final String UNREASONBLE_LENGTH = "Unreasonable length = ";
    private DataInput in;

    static public BinaryInputArchive getArchive(InputStream strm) {
        return new BinaryInputArchive(new DataInputStream(strm));
    }

    public BinaryInputArchive(DataInput in) {
        this.in = in;
    }

    public byte readByte(String tag) throws IOException {
        return in.readByte();
    }

    public boolean readBool(String tag) throws IOException {
        return in.readBoolean();
    }

    public int readInt(String tag) throws IOException {
        return in.readInt();
    }

    public long readLong(String tag) throws IOException {
        return in.readLong();
    }

    public float readFloat(String tag) throws IOException {
        return in.readFloat();
    }

    public double readDouble(String tag) throws IOException {
        return in.readDouble();
    }

    public String readString() throws IOException {
        int len = in.readInt();
        if (len == -1) return null;
        checkLength(len);
        byte b[] = new byte[len];
        in.readFully(b);
        //return new String(b, "UTF8");
        return new String(b, "ASCII");
    }

    public byte[] readBuffer(String tag) throws IOException {
        int len = readInt(tag);
        if (len == -1) return null;
        checkLength(len);
        byte[] arr = new byte[len];
        in.readFully(arr);
        return arr;
    }

    @Override
    public void readRecord(Record r, String tag) throws IOException {
        r.deserialize(this, tag);
    }

    public void startRecord(String tag) throws IOException {
    }

    public void endRecord(String tag) throws IOException {
    }

    public void endVector(String tag) throws IOException {
    }

    public void endMap(String tag) throws IOException {
    }

    private void checkLength(int len) throws IOException {
        if (len < 0 || len > Constants.MAX_BUFFER + 1024) {
            throw new IOException(UNREASONBLE_LENGTH + len);
        }
    }
}
