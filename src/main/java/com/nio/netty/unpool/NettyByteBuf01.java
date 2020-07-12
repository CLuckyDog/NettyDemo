package com.nio.netty.unpool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args) {

        /*创建一个ByteBuf*/
        /*
        * 说明
        * 1.创建对象，该对象包含一个数组arr，是一个byte[10]
        * 2.在netty的buffer中，不需要使用flip，相对于NIO的buffer更方便
        *       底层维护了一个readerindex 和 writeindex
        * 3.通过readerindex 和 writeindex 和 capacity，将 buffer 分成三个区域
        * 0---readerindex 已经读取的区域
        * readerindex---writeindex  可读的区域
        * writeindex --- capacity  可写的区域
        * */
        ByteBuf byteBuf = Unpooled.buffer(10);
        for (int i=0;i<10;i++){
            byteBuf.writeByte(i);
        }

        System.out.println("capacity = "+byteBuf.capacity());

        /*输出*/
        for (int i=0;i<byteBuf.capacity();i++){
//            System.out.println(byteBuf.getByte(i));
            System.out.println(byteBuf.readByte()   );
        }

    }
}
