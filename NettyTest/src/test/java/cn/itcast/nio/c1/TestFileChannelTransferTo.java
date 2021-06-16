package cn.itcast.nio.c1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        // 文件拷贝
        try (
                FileChannel fromChannel = new FileInputStream("data.txt").getChannel();
                FileChannel toChannel = new FileOutputStream("data-copy.txt").getChannel()
        ) {
            // 效率高，底层会利用操作系统的零拷贝进行优化
            // 文件传输数据大小上限：2G
            // fromChannel.transferTo(0, fromChannel.size(), toChannel);

            long size = fromChannel.size();
            for (long left = size; left > 0;) {
                left -= fromChannel.transferTo(size - left, left, toChannel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
