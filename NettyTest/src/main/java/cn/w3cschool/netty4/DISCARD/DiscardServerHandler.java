package cn.w3cschool.netty4.DISCARD;

import io.netty.buffer.ByteBuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 处理服务端 channel.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // 默默地丢弃收到的数据
        // ((ByteBuf) msg).release(); // (3)

        System.out.println("DiscardServerHandler.release ~~");

        ByteBuf in = (ByteBuf) msg;
        try {
            // 不应答
            // while (in.isReadable()) { // (1)
            //    System.out.println("readByte: " + (char) in.readByte());
            //    System.out.flush();
            // }

            // 12314.300000 ??
            // 完整输出接收到的内容
            // System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));

            // 应答，返回给客户端
            ctx.write(msg);
            ctx.flush();
            // ctx.writeAndFlush(msg);

        } finally {
            in.release(); // (2)
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

}
