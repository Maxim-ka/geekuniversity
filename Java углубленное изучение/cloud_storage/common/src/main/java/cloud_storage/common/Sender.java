package cloud_storage.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class Sender extends ChannelOutboundHandlerAdapter {



    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        byte[] mesegByte = ((String) msg).getBytes();
        ByteBuf byteBuf = ctx.alloc().buffer(mesegByte.length);
        byteBuf.writeBytes(mesegByte);
        ctx.write(byteBuf);
        ctx.flush();
    }


}
