package cn.itcast.nio.c1;

import lombok.extern.slf4j.Slf4j;
import static cn.itcast.nio.c1.ByteBufferUtil.debugAll;

import java.nio.ByteBuffer;

@Slf4j
public class TestByteBufferAllocate {

    public static void main(String[] args) {
        // ByteBuffer 只能固定空间容量

        /*
         * class java.nio.HeapByteBuffer 堆内存，读写效率低。和 Java 普通对象一样使用堆内存，受到垃圾回收 GC 的影响
         * class java.nio.DirectByteBuffer 直接内存，读写效率高（少一次拷贝）。系统内存，不会受到 GC 的影响。分配内存的效率低下，使用不当不释放会造成内存泄露
         */
        // System.out.println(ByteBuffer.allocate(10).getClass());
        // System.out.println(ByteBuffer.allocateDirect(10).getClass());

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[] { 0x61, 0x62, 0x63, 0x64 });
        buffer.flip();

        // rewind, position 重置为 0
        buffer.get(new byte[4]);
        debugAll(buffer);
        buffer.rewind();
        debugAll(buffer);

    }
}
