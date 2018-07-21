package cloud_storage.server;

import cloud_storage.common.*;
import io.netty.channel.*;

import java.io.*;
import java.util.Vector;

class ClientHandler extends ChannelInboundHandlerAdapter{

    private final ActionOnFile action = new ActionOnFile();
    private final Vector<String> listOfOpenChannels;
    private String rootUserDirectory;
    private Sender sender;

    ClientHandler(Vector<String> listOfOpenChannels) {
        this.listOfOpenChannels = listOfOpenChannels;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sender = new Sender(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof  String){
            String[] strings = ((String) msg).split("\\s+");
            if (strings[0].equals(SCM.AUTH)) {
                rootUserDirectory = strings[2];
                listOfOpenChannels.add(strings[2]);
                return;
            }
            if (strings[0].equals(SCM.DISCONNECT)){
                listOfOpenChannels.remove(rootUserDirectory);
                ChannelFuture future = ctx.channel().disconnect();
                future.addListener(ChannelFutureListener.CLOSE);
                return;
            }
        }
        if (msg instanceof TransferFile){
            TransferFile file = (TransferFile) msg;
            action.writeFile(rootUserDirectory, file);
            // TODO: 20.07.2018 Отправка ответа пока по последней части файла.
            if(file.getPortion() == file.getTotal()) sendReply();
            return;
        }
        if (msg instanceof RequestCatalog){
            RequestCatalog request = (RequestCatalog)msg;
            sender.actionWithFile(request.getString(), request.getCatalog());
        }
    }

    private void sendReply(){
        sender.send(new RequestCatalog(String.format("%s %s", SCM.OK, rootUserDirectory), new File(rootUserDirectory).listFiles()));
    }
}
