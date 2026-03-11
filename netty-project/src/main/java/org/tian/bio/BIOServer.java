package org.tian.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ：tianLe
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6667);

        System.out.println("服务器启动了");

        while (true) {
            // 阻塞
            final Socket socket = serverSocket.accept();
            System.out.println("建立了一个新的链接");
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);

                }
            });
        }

    }


    public static void handler(Socket socket) {
        try {
            System.out.println("线程信息 id=" + Thread.currentThread().getId() + "名字=" + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            while (true) {
                // 阻塞
                int read = inputStream.read(bytes);
                if (read != -1) {
                    String request = new String(bytes, 0, read, StandardCharsets.UTF_8);
                    System.out.println(request);

                    String response = "server-reply: received -> " + request;
                    outputStream.write(response.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            System.out.println("关闭和client的链接");

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
