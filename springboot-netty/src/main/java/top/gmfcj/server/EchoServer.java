package top.gmfcj.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import top.gmfcj.handler.EchoServerHandler;

import java.net.InetSocketAddress;

@Component
public class EchoServer {

    private static final Logger LOG = LoggerFactory.getLogger(EchoServer.class);

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;

    public ChannelFuture start(String hostname, int port) throws Exception {

        final EchoServerHandler serverHandler = new EchoServerHandler();

        ChannelFuture f = null;
        try {
            //ServerBootstrap负责初始化netty服务器，并且开始监听端口的socket请求
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(hostname,port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 为监听客户端read/write事件的Channel添加用户自定义的ChannelHandler
                            socketChannel.pipeline().addLast(serverHandler);
                        }
                    });

            f = b.bind().sync();
            channel = f.channel();
            LOG.info("======EchoServer启动成功!!!=========");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (f != null && f.isSuccess()) {
                LOG.info("Netty server listening " + hostname + " on port " + port + " and ready for connections...");
            } else {
                LOG.error("Netty server start up Error!");
            }
        }
        return f;
    }

    /**
     * 停止服务
     */
    public void destroy() {
        LOG.info("Shutdown Netty Server...");
        if(channel != null) { channel.close();}
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        LOG.info("Shutdown Netty Server Success!");
    }
}
