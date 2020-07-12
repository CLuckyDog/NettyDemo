package com.nio.netty.unpool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import static io.netty.util.CharsetUtil.UTF_8;

public class NettyByteBuf02 {
    public static void main(String[] args) {

        /*
        * 创建    ByteBuf
        * */
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello netty!为什么", UTF_8);

        /*使用相关的 API*/
        if (byteBuf.hasArray()){
            byte[] array = byteBuf.array();
            System.out.println(array.length);
            /*将 array 转成字符串*/
            System.out.println(new String(array,0,byteBuf.writerIndex(),UTF_8));
            System.out.println("bytebuf = "+byteBuf);
            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());
            System.out.println(byteBuf.readableBytes());
            System.out.println(byteBuf.getCharSequence(1,5,UTF_8));
        }
    }
}
