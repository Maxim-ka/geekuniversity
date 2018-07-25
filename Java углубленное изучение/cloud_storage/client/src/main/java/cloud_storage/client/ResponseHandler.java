package cloud_storage.client;

import cloud_storage.client.GUI.Controller;
import cloud_storage.common.ActionOnFile;
import cloud_storage.common.RequestCatalog;
import cloud_storage.common.SCM;
import cloud_storage.common.TransferFile;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;

import java.io.File;

class ResponseHandler extends ChannelInboundHandlerAdapter{

    private Controller controller;
    private ActionOnFile action;

    ResponseHandler(Controller controller) {
        this.controller = controller;
        action = new ActionOnFile();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof  RequestCatalog){
            RequestCatalog request = (RequestCatalog) msg;
            String string = request.getCommand();
            String[] strings = string.split("\\s+");
            if (strings[0].equals(SCM.AUTH)) {
                if (strings[1].equals(SCM.OK)){
                    Client.getInstance().setAuthorized(true);
                    controller.getLogin().clear();
                    controller.getPass().clear();
                    controller.authorization();
                    showResponse(request.getCurrentCatalog(), request.getCatalog());
                    return;
                }
                Client.getInstance().setAuthorized(false);
                // TODO: 20.07.2018 вывод сообщений о неудачной авторизации
                return;
            }
            if (strings[0].equals(SCM.OK)){
                showResponse(request.getCurrentCatalog(), request.getCatalog());
                return;
            }
            if (strings[0].equals(SCM.BAD)){

            }
        }
        if (msg instanceof TransferFile){
            TransferFile file = (TransferFile) msg;
            action.writeFile(controller.getCurrentDirectory().getText(), file);
            // TODO: 20.07.2018 обновление экрана пока последней части файла
            if(file.getPortion() == file.getTotal()) updateCurrentDirectory(file.getFile());
        }
    }

    private void updateCurrentDirectory(final File file){
        Platform.runLater(() ->{
            controller.getData().add(file);
        });
    }

    private void showResponse(final String currentDirectory, final File[] listFilesServer){
        Platform.runLater(() ->{
            controller.getDirectoryOnServer().setText(currentDirectory);
            controller.getCatalogUser().setAll(listFilesServer);
        });
    }
}
