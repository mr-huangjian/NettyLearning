package cn.itcast.nio.c1;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class TestByteBufferExam {
    public static void main(String[] args) {

        // 两个数据合在一块，称为黏包，一般是由于服务器一次性接收到打包的多条数据
        // 两个数据分割开来，称为半包，一般是由于服务器缓冲区大小的原因造成的

        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        byteBuffer.put("Hello, World\nI'm Jack\nHo".getBytes());
        split(byteBuffer);
        byteBuffer.put("w are you?\n".getBytes());
        split(byteBuffer);
    }

    private static void split(ByteBuffer source) {
        // 切换到读模式
        source.flip();

        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n') {
                int length = i + 1 - source.position();
                ByteBuffer target = ByteBuffer.allocate(length);

                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }

                {
                    // 调试：输出 target 内容
                    target.flip();
                    CharBuffer charBuffer = StandardCharsets.UTF_8.decode(target);
                    System.out.println(String.format("target: [%s]", charBuffer.toString()));
                }

                ByteBufferUtil.debugAll(target);
            }
        }

        // 切换到写模式，继续接收新内容
        source.compact();
    }

}
