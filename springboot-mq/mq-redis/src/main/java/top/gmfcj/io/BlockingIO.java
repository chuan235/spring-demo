package top.gmfcj.io;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 阻塞IO
 */
public class BlockingIO {

    public static void main(String[] args) throws Exception {
        byte[] bytes = new byte[1024];
        ServerSocket serverSocket = new ServerSocket();
        System.out.println("wait connection");
        // bind
        serverSocket.bind(new InetSocketAddress(8080));
        // accept阻塞
        Socket accept = serverSocket.accept();
        System.out.println("connection success");
        // read阻塞
        System.out.println("wait data");
        InputStream inputStream = accept.getInputStream();
        int len = 0;
        PrintStream out = System.out;
        while ((len = inputStream.read(bytes)) != -1) {
            System.out.println("data end");
            out.print(new String(bytes));
        }
    }

}
