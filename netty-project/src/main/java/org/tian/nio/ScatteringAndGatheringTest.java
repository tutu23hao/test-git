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

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7001);
        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8;
        while (true) {

            int byteRead = 0;
            while (byteRead < messageLength) {
                long read = socketChannel.read(byteBuffers);
                byteRead += read;
            }
            Arrays.stream(byteBuffers).map(byteBuffer -> "position=" + byteBuffer.position() + ", limit= " + byteBuffer.limit())
                    .forEach(System.out::println);

            Arrays.stream(byteBuffers).forEach(ByteBuffer::flip);

            int byteWrite = 0;
            while (byteWrite < messageLength) {
                long write = socketChannel.write(byteBuffers);
                byteWrite += write;
            }
//            Arrays.stream(byteBuffers).map(byteBuffer -> "position=" + byteBuffer.position() + ", limit= " + byteBuffer.limit())
//                    .forEach(System.out::println);
            

            Arrays.asList(byteBuffers).forEach(ByteBuffer::clear);


        }


    }
}
