package cn.itcast.nio.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static cn.itcast.nio.c1.ByteBufferUtil.debugRead;

@Slf4j
public class Server {

    public static void main(String[] args) throws IOException {
        // 使用 NIO 来理解阻塞模式

        ByteBuffer buffer = ByteBuffer.allocate(16);

        // 创建服务器
        ServerSocketChannel channel = ServerSocketChannel.open();

        // 绑定监听端口
        channel.bind(new InetSocketAddress(8088));

        List<SocketChannel> socketChannels = new ArrayList<>();

        while (true) {
            // 建立与客户端的连接，与之通信
            log.debug("connecting...");

            // accept 是一个阻塞方法，线程停止运行，直到连接建立后才继续运行
            SocketChannel socketChannel = channel.accept();
            log.debug("connected... {}", socketChannel);

            socketChannels.add(socketChannel);

            for (SocketChannel ch : socketChannels) {
                // 接收客户端发送的数据
                log.debug("before read... {}", ch);
                ch.read(buffer); // 阻塞方法
                buffer.flip();
                debugRead(buffer); // 快捷键：Alt+Enter
                buffer.clear();
                log.debug("after read... {}", ch);
            }
        }

    }
}


















