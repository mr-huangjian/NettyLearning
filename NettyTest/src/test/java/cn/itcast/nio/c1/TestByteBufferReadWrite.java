package cn.itcast.nio.c1;

import lombok.extern.slf4j.Slf4j;
import static cn.itcast.nio.c1.ByteBufferUtil.debugAll;

import java.nio.ByteBuffer;

@Slf4j
public class TestByteBufferReadWrite {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put((byte) 0x61); // 写入十六进制的 a
        debugAll(byteBuffer);
        byteBuffer.put(new byte[] { 0x62, 0x63, 0x64 }); // 写入十六进制的 b c d
        debugAll(byteBuffer);

        // System.out.println(byteBuffer.get()); // 还未切换到读模式，buffer position is 5，output is 0

        /*
         * buffer.get() 方法会让指针向后移，若想重复读取数据，
         * 可以调用 buffer.rewind 方法，将 position 重置为 0
         * 或者调用 buffer.get(i) 方法，获取索引 i 的内容，它不会移动指针
         */

        System.out.println("\nbuffer 切换到读模式：");
        byteBuffer.flip(); // buffer 切换到读模式
        System.out.println(byteBuffer.get()); // 已切换到读模式，buffer position is 1，output is 97 (十进制)、
        debugAll(byteBuffer);

        System.out.println("\nbuffer 执行 compact 操作，切换到写模式：");
        System.out.println(String.format("1. position: %d, limit: %d", byteBuffer.position(), byteBuffer.limit())); // 1. position: 1, limit: 4
        /*
         * !! compact() 的骚操作：
         * 未读数据位置上的值不变，只是将未读数据位置上的值，整体拷贝覆盖到已读位置上
         */
        byteBuffer.compact();
        debugAll(byteBuffer);
        System.out.println(String.format("2. position: %d, limit: %d", byteBuffer.position(), byteBuffer.limit())); // 2. position: 3, limit: 10

        System.out.println("\nbuffer 再次写入数据");
        byteBuffer.put(new byte[] { 0x65, 0x66, 0x67 });
        debugAll(byteBuffer);
    }
}
