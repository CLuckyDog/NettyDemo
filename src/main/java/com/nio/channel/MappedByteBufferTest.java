package com.nio.channel;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("E:\\workspace\\NettyDemo\\TestFiles\\test01.txt", "rw");
        FileChannel channel = file.getChannel();

        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        map.put(0,(byte) 'X');
        map.put(3,(byte) 'Y');
//        map.put(5,(byte) 'X');
        file.close();
        System.out.println("修改成功！");

    }
}
