package cn.itcast.nio.c1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static cn.itcast.nio.c1.ByteBufferUtil.debugAll;

public class TestScatteringReads {
    public static void main(String[] args) {

        // 数据分割读取（分散读）

        try (FileChannel fileChannel = new RandomAccessFile("3parts.txt", "r").getChannel()) {
            ByteBuffer b1 = ByteBuffer.allocate(3);
            ByteBuffer b2 = ByteBuffer.allocate(3);
            ByteBuffer b3 = ByteBuffer.allocate(5);

            fileChannel.read(new ByteBuffer[] { b1, b2, b3});
            b1.flip();
            b2.flip();
            b3.flip();

            debugAll(b1);
            debugAll(b2);
            debugAll(b3);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
