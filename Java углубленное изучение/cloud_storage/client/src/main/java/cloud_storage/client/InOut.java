package cloud_storage.client;

import cloud_storage.common.SCM;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

class InOut extends ChannelInboundHandlerAdapter{

    private String authorizationMessage;

    public String getAuthorizationMessage() {
        return authorizationMessage;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String string = (String)msg;
        System.out.println(string);
//        if (string.equals(SCM.OK)) authorizationMessage = string;
        if (string.startsWith(SCM.AUTH)) authorizationMessage = SCM.OK;
    }
}
