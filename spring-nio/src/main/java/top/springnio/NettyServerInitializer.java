package top.springnio;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyServerHandler nettyServerHandler;

    // 绑定handler
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 配置解码和编码器
        // 以 \n 为结尾分割的解码器
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        // 字符串的解码和编码器
        pipeline.addLast("decoder", new StringDecoder(Charset.forName("ASCII")));
        pipeline.addLast("encoder", new StringEncoder(Charset.forName("ASCII")));
        // 自定义的Handler
        pipeline.addLast("handler", nettyServerHandler);
        System.out.println(Thread.currentThread().getName() + " ---- initChannel");
    }


}

