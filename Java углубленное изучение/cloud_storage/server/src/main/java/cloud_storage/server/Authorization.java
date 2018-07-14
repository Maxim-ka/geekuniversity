package cloud_storage.server;

import cloud_storage.common.SCM;
import cloud_storage.server.dataBase.AuthService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;

public class Authorization extends ChannelInboundHandlerAdapter{

    private final AuthService authService = new AuthService();
    private final ObjectEncoder objectEncoder = new ObjectEncoder();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String string = (String)msg;
        String[] strings = string.split("\\s+");
        if (string.startsWith(SCM.AUTH) && strings.length == 3){
            authService.connect();
            String catalogUser = authService.getCatalogUser(strings[1], strings[2]);
            if (catalogUser != null){
                if (new File(catalogUser).exists()){
                    File[] catalog = new File(catalogUser).listFiles();

                }
                String message = String.format("%s %s", strings[0], catalogUser);
//                ctx.fireChannelRead(message);
                ctx.channel().write(message);
                ctx.pipeline().remove(this);
            }
            authService.disconnect();
        }
        // TODO: 14.07.2018 отправить обратно сообщение о неправильной авторизации  и закрыть канал
    }
}
