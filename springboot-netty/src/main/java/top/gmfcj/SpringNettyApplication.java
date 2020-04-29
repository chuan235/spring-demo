package top.gmfcj;

import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.gmfcj.server.EchoServer;

@SpringBootApplication
public class SpringNettyApplication implements CommandLineRunner {
    @Autowired
    private EchoServer echoServer;

    public static void main(String[] args) {
        SpringApplication.run(SpringNettyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture future = echoServer.start("127.0.0.1", 9000);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                echoServer.destroy();
            }
        });
        //服务端管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
        future.channel().closeFuture().syncUninterruptibly();
    }
}
