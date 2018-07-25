package cloud_storage.server;

import cloud_storage.common.*;
import io.netty.channel.*;

import java.io.*;
import java.util.Vector;

class ClientHandler extends ChannelInboundHandlerAdapter{

    private final ReceiverCollectorFile writer = new ReceiverCollectorFile();
    private final FileSendingHandler fileSendingHandler = new FileSendingHandler();
    private final Vector<String> listOfOpenChannels;
    private String rootUserDirectory;
    private int countPart = 1;

    ClientHandler(Vector<String> listOfOpenChannels) {
        this.listOfOpenChannels = listOfOpenChannels;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelFuture future = ctx.channel().closeFuture();
        future.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof  String){
            String[] strings = ((String) msg).split("\\s+");
            if (strings[0].equals(SCM.AUTH)) {
                rootUserDirectory = strings[1];
                listOfOpenChannels.add(strings[1]);
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
            writer.writeFile(rootUserDirectory, file);
            if (file.getPortion() != file.getTotal()){
                ++countPart;
            }
            if(countPart == file.getTotal()){
                ctx.writeAndFlush(new RequestCatalog(SCM.OK, rootUserDirectory, new File(rootUserDirectory).listFiles()));
                countPart = 1;
            }else if (file.getPortion() == file.getTotal()){
                ctx.writeAndFlush(new RequestCatalog(SCM.BAD, rootUserDirectory, new File(rootUserDirectory).listFiles()));
                countPart = 1;
            }
            return;
        }
        if (msg instanceof RequestCatalog){
            RequestCatalog request = (RequestCatalog)msg;
            switch (request.getCommand()){
                case SCM.UPDATE:
                    ctx.writeAndFlush(new RequestCatalog(SCM.OK, request.getCurrentCatalog(), new File(request.getCurrentCatalog()).listFiles()));
                    break;
                default:
                    RequestCatalog requestCatalog = fileSendingHandler.actionWithFile(ctx.channel(), request.getCommand(), request.getCurrentCatalog(), request.getCatalog());
                    ctx.writeAndFlush(requestCatalog);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
        System.out.println(cause.toString());
    }
}
