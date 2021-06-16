package cn.itcast.nio.c1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static cn.itcast.nio.c1.ByteBufferUtil.debugAll;

@Slf4j
public class TestByteBufferRead {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put(new byte[] { 'a', 'b', 'c', 'd' });
        byteBuffer.flip();

        byteBuffer.get(new byte[4]); // 从头开始全部读完 (limit: 4)
        debugAll(byteBuffer);

        // rewind 从头开始读
        byteBuffer.rewind();
        System.out.println((char) byteBuffer.get()); // a

        // mark  标记现在的 position，
        // reset 重置回原来 mark 标记的 position

        byteBuffer.mark(); // position: 1
        System.out.println((char) byteBuffer.get()); // b
        System.out.println((char) byteBuffer.get()); // c

        byteBuffer.reset(); // position: 1
        System.out.println((char) byteBuffer.get()); // b

        // get(i) 只做读取，不改变指针 position
        byteBuffer.rewind();
        byteBuffer.get(2); // position: 0
        debugAll(byteBuffer);

    }
}
