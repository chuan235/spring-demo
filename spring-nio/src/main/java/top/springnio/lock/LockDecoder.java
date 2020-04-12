package top.springnio.lock;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class LockDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int type = byteBuf.readInt();
        int contentLen = byteBuf.readInt();
        // 读backId
        byte[] backId = new byte[6];
        byteBuf.readBytes(backId);
        String sessionId = new String(backId, Charset.forName("ASCII"));
        // 组装协议头
        LockHeader header = new LockHeader(type, contentLen, sessionId);
        // 获取消息体
        byte[] body = byteBuf.readBytes(byteBuf.readableBytes()).array();
        LockMessage lockMessage = new LockMessage(header, new String(body,Charset.forName("ASCII")));
        list.add(lockMessage);
    }
}
