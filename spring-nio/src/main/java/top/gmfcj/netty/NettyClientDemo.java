package top.gmfcj.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.io.InputStream;
import java.util.Date;
import java.util.Scanner;

public class NettyClientDemo {

    public static void main(String[] args) throws Exception {
        // Client辅助启动类
        Bootstrap bootstrap = new Bootstrap();
        // 客户端的线程组
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            // 配置bootstrap
            bootstrap.group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        // 创建NioSocketChannel成功之后，进行初始化时，将ChannelHandler设置到ChannelPipeline中，同样，用于处理网络I/O事件
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringEncoder());
                        }
                    });
            // 发起异步连接操作  同步方法待成功
            Channel channel = bootstrap.connect("127.0.0.1", 9000).channel();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (scanner.hasNext()) {
                    String msg = scanner.next();
                    channel.writeAndFlush(new Date() + ": " + msg);
                }
            }
        } finally {
            // 优雅退出，释放NIO线程组
            clientGroup.shutdownGracefully();
        }
    }
}
