package cn.itcast.netty.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@Slf4j
public class EventLoopServer {
    public static void main(String[] args) {
        new ServerBootstrap()
                /*
                First new NioEventLoopGroup(): boss 只负责下面的 NioServerSocketChannel 的 accept 事件
                Second new NioEventLoopGroup(): worker 只负责下面的 NioSocketChannel 的 read、write 事件
                 */
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // header -> h1 -> h3 -> h2 -> h4 -> h5 -> h6 -> tail

                        ch.pipeline().addLast("h1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("1");

                                ByteBuf byteBuf = (ByteBuf) msg;
                                String string = byteBuf.toString(Charset.defaultCharset()) + " -> handler1";
                                log.debug(string);

                                super.channelRead(ctx, string);
                            }
                        });
                        ch.pipeline().addLast("h3", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("3");

                                String string = msg.toString() + " -> handler3";
                                log.debug(string);

                                super.channelRead(ctx, string);
                                // ctx.fireChannelRead(msg); 同上一个方法一样的
                            }
                        });
                        ch.pipeline().addLast("h2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("2");

                                String string = msg.toString() + " -> handler2";
                                log.debug(string);

                                ByteBuf byteBuf = ctx.alloc().buffer().writeBytes("server...".getBytes());
                                ch.writeAndFlush(byteBuf);

                                /**
                                 * ch.writeAndFlush(byteBuf); 从 tail 向前找出站 handler
                                 * ctx.writeAndFlush(byteBuf); 从当前 handler 向前找出站 handler
                                 */

                                // 因为后面没有入站 handler 了，不需要向下一个 handler 传递，
                                // super.channelRead(ctx, msg);
                            }
                        });
                        ch.pipeline().addLast("h4", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("4");
                                super.write(ctx, msg, promise);
                            }
                        });
                        ch.pipeline().addLast("h5", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("5");
                                super.write(ctx, msg, promise);
                            }
                        });
                        ch.pipeline().addLast("h6", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("6");
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                .bind(8088);
    }
}
