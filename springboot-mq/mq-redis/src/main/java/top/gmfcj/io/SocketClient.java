package top.gmfcj.io;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8080);
        OutputStream outputStream = socket.getOutputStream();
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String input = scanner.next();
            outputStream.write(input.getBytes());
            outputStream.flush();
        }
    }
}
