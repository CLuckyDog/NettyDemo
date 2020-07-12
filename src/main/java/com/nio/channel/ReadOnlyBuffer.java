package com.nio.channel;

import java.nio.ByteBuffer;

/**
 * Buffer只读化限制
 * */
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        for (int i=0;i<64;i++){
            byteBuffer.put((byte) i);
        }

        /*读取前切换*/
        byteBuffer.flip();

        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        /*读取*/
        while (readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }

        /*写入前切换*/
//        readOnlyBuffer.flip();
//        readOnlyBuffer.put((byte) 100);  /*java.nio.ReadOnlyBufferException*/

    }
}
