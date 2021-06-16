package cn.itcast.nio.c1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class TestGatheringWrites {
    public static void main(String[] args) {

        // ByteBuffer 组合写入（集中写）
        ByteBuffer b1 = StandardCharsets.UTF_8.encode("hello"); // 5个字节
        ByteBuffer b2 = StandardCharsets.UTF_8.encode("world"); // 5个字节
        ByteBuffer b3 = StandardCharsets.UTF_8.encode("您好");   // 3个字节 * 2

        try (FileChannel fileChannel = new RandomAccessFile("combine.txt", "rw").getChannel()) {
            fileChannel.write(new ByteBuffer[]{ b1, b2, b3 });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
