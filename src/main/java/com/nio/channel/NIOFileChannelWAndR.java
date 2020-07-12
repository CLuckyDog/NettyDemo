package com.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannelWAndR {
    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("E:\\workspace\\NettyDemo\\TestFiles\\test01.txt");
        FileChannel fisChannel = fis.getChannel();

        FileOutputStream fos = new FileOutputStream("test02.txt");
        FileChannel fosChannel = fos.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {/*循环读取数据*/
            /*这里有个重要的操作必须进行  clean  重置buffer成员变量*/
            buffer.clear();/*清空buffer*/

            int read = fisChannel.read(buffer);
            if (read == -1) {
                break;
            }
            /*将buffer中的数据写入到 另外一个txt文件中*/
            buffer.flip();
            fosChannel.write(buffer);
        }
        fis.close();
        fos.close();
    }
}
