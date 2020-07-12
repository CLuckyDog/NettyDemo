package com.nio.channel;

import java.nio.ByteBuffer;

/**
 *  Buffer 类型化
 * */
public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer buffer=ByteBuffer.allocate(64);

        buffer.putInt(100);
        buffer.putLong(10L);
        buffer.putChar('整');
        buffer.putShort((short) 5);

        buffer.flip();
        /*get顺序要和put顺序一致，否则报错或者读取错误数据*/
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
    }
}
