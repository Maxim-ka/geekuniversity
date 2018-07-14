package cloud_storage.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

class ClientChannel extends ChannelInboundHandlerAdapter{

    private Channel channel;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        channel = ctx.channel();
        String string = (String) msg;
        System.out.println(string);

//        channel.write(string);
//        channel.flush();
    }
}
