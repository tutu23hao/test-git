package org.tian.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author ：tianLe
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {

        //  ServerSocketChannel 和 SocketChannel

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8;
        while (true) {
            long read = socketChannel.read(byteBuffers);

            if (read <= messageLength) {
                Arrays.asList(byteBuffers).stream().map(byteBuffer -> {
                });
            }
        }


    }
}
