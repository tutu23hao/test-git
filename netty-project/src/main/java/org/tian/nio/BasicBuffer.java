package org.tian.nio;

import java.nio.IntBuffer;

/**
 * @author ：tianLe
 */
public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);

        // 存入数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);

        }

        intBuffer.flip();
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
