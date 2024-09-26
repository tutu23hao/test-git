package org.tian.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author ：tianLe
 */
public class NIOFileChannel01 {

    public static void main(String[] args) throws FileNotFoundException {
        String str = "hello tianle";
        FileOutputStream fileOutputStream = new FileOutputStream(new File("file01.txt"));
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 将数据放入缓存区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes(StandardCharsets.UTF_8));
        // 重置position   然后将byteBuffer 写入fileChannel
        byteBuffer.flip();

        try {
            fileChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
