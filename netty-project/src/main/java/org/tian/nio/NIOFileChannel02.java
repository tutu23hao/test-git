package org.tian.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author ：tianLe
 */
public class NIOFileChannel02 {

    public static void main(String[] args) throws IOException {
        File file = new File("file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);


        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(3);

        FileOutputStream fileOutputStream = new FileOutputStream(new File("file02.txt"));
        FileChannel fileOutputChannel = fileOutputStream.getChannel();


        while (true) {
            byteBuffer.clear();
            int read = fileChannel.read(byteBuffer);
            if (read == -1) {
                break;
            }
            byteBuffer.flip();
            fileOutputChannel.write(byteBuffer);
        }
        fileInputStream.close();

        fileOutputStream.close();

    }
}
