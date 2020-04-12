package top.gmfcj.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ByteBufferOutputStream extends OutputStream {

    ByteBuffer bb;

    public ByteBufferOutputStream(ByteBuffer bb) {
        this.bb = bb;
    }

    @Override
    public void write(int b) throws IOException {
        bb.put((byte) b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        bb.put(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        bb.put(b, off, len);
    }
}
