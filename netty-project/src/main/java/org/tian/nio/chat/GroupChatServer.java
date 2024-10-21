package org.tian.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author ：tianLe
 */
public class GroupChatServer {
    private static final int PORT = 6667;
    private Selector selector;
    private ServerSocketChannel listenChannel;

    public GroupChatServer() {
        try {
            selector = Selector.open();

            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));

            listenChannel.configureBlocking(false);

            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

    public void listen() {
        while (true) {
            try {
                int count = selector.select();

                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress() + "上线");
                        }

                        if (key.isReadable()) {
                            readData(key);
                        }
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readData(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            int read = channel.read(buffer);
            if (read > 0) {
                String msg = new String(buffer.array());
                System.out.println("from客户端 " + msg);
                sendInfoToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发信息中");

        for (SelectionKey selectedKey : selector.keys()) {
            Channel channel = selectedKey.channel();

            if (channel instanceof SocketChannel && channel != self) {
                SocketChannel targetChannel = (SocketChannel) channel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                targetChannel.write(buffer);
            }

        }
    }
}
