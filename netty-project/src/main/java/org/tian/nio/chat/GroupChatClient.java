package org.tian.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author ：tianLe
 */
public class GroupChatClient {
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 6667;
    private Selector selector;

    private SocketChannel socketChannel;

    private String userName;

    public GroupChatClient() {
        try {
            selector = Selector.open();

            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));

            socketChannel.configureBlocking(false);

            socketChannel.register(selector, SelectionKey.OP_READ);

            userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName + "is ok ...");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        GroupChatClient client = new GroupChatClient();
        // 监听服务端消息
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    client.readInfo();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            client.sendInfo(s);
        }

    }

    private void sendInfo(String msg) {
        msg = userName + "说：" + msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readInfo() {
        int count = 0;
        try {
            count = selector.select();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (count <= 0) {
            return;
        }
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {


            SelectionKey key = iterator.next();


            if (key.isReadable()) {
                SocketChannel channel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int read = 0;
                try {
                    read = channel.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (read > 0) {
                    String msg = new String(buffer.array());
                    System.out.println(msg.trim());
                }

            }


            iterator.remove();

        }

    }
}
