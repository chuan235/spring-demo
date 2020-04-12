package top.springnio.lock;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class LockEncoder extends MessageToByteEncoder<LockMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, LockMessage lockMessage, ByteBuf byteBuf) throws Exception {
        // 将Message转换成二进制数据
        LockHeader header = lockMessage.getHead();
        // 写入Header信息 => 写入的顺序就是协议的顺序
        byteBuf.writeInt(header.getType());
        byteBuf.writeInt(lockMessage.getBody().length());
        byteBuf.writeBytes(header.getSessionId().getBytes());
        // 写入消息主体信息
        byteBuf.writeBytes(lockMessage.getBody().getBytes());
    }
}
