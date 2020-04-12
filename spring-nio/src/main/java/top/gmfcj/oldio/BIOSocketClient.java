package top.gmfcj.oldio;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * 客户端 每两秒向服务器发送一条数据
 */
public class BIOSocketClient {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1",8000);
                while(true){
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write((new Date()+ ": hello ").getBytes());
                    outputStream.flush();
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
