package top.gmfcj.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServerDemo {

    public static void main(String[] args) throws Exception{
        // 创建bootstrap 用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 创建boss线程组
        EventLoopGroup boss = new NioEventLoopGroup();
        // 创建work线程组
        EventLoopGroup work = new NioEventLoopGroup();
        try {
            System.out.println("--- start nio ---");
            // 开启配置启动器
            bootstrap.group(boss,work) // 配置线程组
                    // 配置channel类型为nio模型
                    .channel(NioServerSocketChannel.class)
                    // 设置事件处理
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            // 添加字符串解码器
                            channel.pipeline().addLast(new StringDecoder());
                            // 添加一个简单的消息处理器
                            channel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
                                    System.out.println(msg);
                                }
                            });
                        }
                    });
            // 端口绑定
            ChannelFuture channelFuture = bootstrap.bind(9000).sync();
            if (channelFuture.isSuccess()) {
                System.out.println("启动Netty服务成功，端口号：" + 9000);
            }
            // 关闭连接
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 关闭线程资源
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

}
