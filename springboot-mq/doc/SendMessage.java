package top.gmfcj.config;

import org.apache.catalina.Server;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

@Component
public class SendMessage {


    public static native void conntect();

    public static void main(String[] args) throws Exception{

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1",5895));
        // 设置accept不阻塞
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        // 注册channel 并且指定感兴趣的事件是accpet
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println(selector.keys().size());
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        writeBuffer.put("received".getBytes());
        writeBuffer.flip();
        while(true){
            int nReady = selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isAcceptable()){
                    // 创建新的连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 设置为非阻塞
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    // 读
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    readBuffer.clear();
                    socketChannel.read(readBuffer);
                    readBuffer.flip();
                    System.out.println("recevied :"+new String(readBuffer.array()));
                    key.interestOps(SelectionKey.OP_WRITE);
                }else if(key.isWritable()){
                    // 写
                    writeBuffer.rewind();
                    System.out.println("send :"+new String(writeBuffer.array()));
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    socketChannel.write(writeBuffer);
                    key.interestOps(SelectionKey.OP_READ);
                }
            }
        }
    }

    public void resive() throws Exception{


//        Socket socket = new Socket("192.168.222.129",8080);
//        OutputStream outputStream = socket.getOutputStream();
//        Scanner scanner = new Scanner(System.in);
//        while(scanner.hasNext()){
//            String input = scanner.next();
//            outputStream.write(input.getBytes());
//            outputStream.flush();
//        }

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
        accept.getInputStream().read(bytes);
        System.out.println("data end");
        System.out.println(new String(bytes));
    }
}
