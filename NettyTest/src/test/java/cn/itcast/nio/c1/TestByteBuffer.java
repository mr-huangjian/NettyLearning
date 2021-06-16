package cn.itcast.nio.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class TestByteBuffer {

    public static void main(String[] args) {
        // https://www.bilibili.com/video/BV1py4y1E7oA?p=6

        /*
            文件读取的两种方式：
            1. 文件输入或输出流 FileInputStream / FileOutputStream
            2. RandomAccessFile
         */

        /*
           `auto-closeable` 代码块补全快捷键：
            new FileInputStream("data.txt").getChannel().twr

            ref: https://blog.csdn.net/Hurricane_m/article/details/89185856
         */
        try (FileChannel fileChannel = new FileInputStream("data.txt").getChannel()) {
            // 准备缓冲区，初始10个字节的空间
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);

            // 查看方法说明卡片，快捷键：Ctrl + J
            // ref https://www.cnblogs.com/appleworld/p/11078436.html

            while (true) {
                // 从 channel 读取数据，意味着向缓冲区写入数据
                int len = fileChannel.read(byteBuffer);
                log.debug("读取到的字节数：{}", len);

                // 没有内容可读了
                if (len == -1) {
                    break;
                }

                // 打印 buffer 内容
                byteBuffer.flip(); // 切换 buffer 为读模式

                // 检测是否还有未读的数据
                while (byteBuffer.hasRemaining()) {
                    byte b = byteBuffer.get(); // 每次只读一个字节
                    log.debug("实际字节：{}", (char) b);
                }

                byteBuffer.clear();  // 切换 buffer 为写模式，进入下一次循环再次写入数据
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
