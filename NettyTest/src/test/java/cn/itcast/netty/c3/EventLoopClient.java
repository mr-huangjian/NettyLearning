package cn.itcast.netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.logging.Level;

@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("localhost", 8088);

        Channel channel = channelFuture.sync().channel();
        log.debug("{}", channel);

        new Thread(() -> {
            // 命令行（控制台）输入文本，发送给服务端
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if ("q".equals(line)) {
                    channel.close(); // async // 关闭 Channel
                    group.shutdownGracefully(); // 关闭 Group
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();

        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.sync();
        log.debug("处理关闭后的操作");
    }

    public static void main0(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 连接服务器：异步非阻塞, main 线程发起调用，真正执行 connect 的是 NIO 线程
                .connect("localhost", 8088);

        // 第一种方法：阻塞当前的主线程，直到 NIO 线程连接建立完毕
        channelFuture.sync();
        Channel channel = channelFuture.channel();
        channel.writeAndFlush("hello, world");

        // 第二种方法：异步回调处理结果
        // 匿名对象只有一个回调方法，可以转换成 LMD 语法，快捷键： alt + enter
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                log.debug("{}", channel); // NIO 线程
                channel.writeAndFlush("hello, world");
            }
        });
    }
}
