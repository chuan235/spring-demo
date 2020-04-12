package top.springnio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

/**
 * ctx.channel().id().asLongText()
 * ctx.channel().id().asShortText()
 */
@Component
@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("---- 开启 channel 持久化channel ----");
        NettySession session = NettySession.buildSession(ctx.channel());

        super.channelActive(ctx);
    }

    // 服务端获取请求的时候被调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接收到远程客户端" + ctx.channel().remoteAddress() + "的消息：" + msg);

//        ByteBuf buffer = (ByteBuf) msg;
//        byte[] requestBuffer = new byte[buffer.readableBytes()];
//        buffer.readBytes(requestBuffer);
//        String content = new String(requestBuffer, "UTF-8");
        System.out.println("netty服务端接受到的消息是：" + msg);
        String responseMsg = "success";
        // 直接刷新发送
        ctx.writeAndFlush(responseMsg);
        // 单只是write并没有发送
        //ctx.write(responseMsg);
    }

    // channelRead方法完成之后调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // flush之后才是真的发送了
//        ctx.flush();
        System.out.println("netty服务端响应成功" + ctx.channel().remoteAddress());
    }

    // 发生异常时调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("服务端捕获 " + ctx.channel().remoteAddress() + "客户端出现异常，关闭ChannelHandlerContext");
        cause.printStackTrace();
        ctx.close();
    }
}
