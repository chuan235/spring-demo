package top.gmfcj.util;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class CommonUtil {

    public static String getString(ByteBuffer buffer, Charset charset) {
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        try {
            decoder = charset.newDecoder();
            // charBuffer = decoder.decode(buffer);//用这个的话，只能输出来一次结果，第二次显示为空
            charBuffer = decoder.decode(buffer);
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String buildString(ByteBuffer buffer, Charset charset) {
        int ps = buffer.position();
        byte[] bs = new byte[ps];
        for (int i = 0; i < ps; i++) {
            bs[i] = buffer.get(i);
        }
        return new String(bs, charset);
    }
}
