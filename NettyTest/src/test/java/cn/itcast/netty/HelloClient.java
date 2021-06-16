package cn.itcast.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bs = new Bootstrap();

        bs.group(new NioEventLoopGroup())
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override // 在连接建立后被调用
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    log.debug("Client handler.initChannel");

                    ch.pipeline().addLast(new StringEncoder());
                }
            });

        // 连接服务器
        log.debug("Client connect");
        ChannelFuture cf = bs.connect("localhost", 8088).sync();

        // 向服务器发送数据
        log.debug("Client write");
        Channel channel = cf.channel();
        channel.writeAndFlush("Hello, Netty!");
    }
}

/**
 * childHandler 与 handler
 * https://blog.csdn.net/jiezhang656/article/details/105197013/
 * http://doc.okbase.net/bdmh/archive/192212.html
 */