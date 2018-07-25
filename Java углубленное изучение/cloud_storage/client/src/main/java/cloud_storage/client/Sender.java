package cloud_storage.client;

import cloud_storage.client.GUI.Controller;
import cloud_storage.common.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import javafx.application.Platform;

import java.io.File;

public class Sender {

    private Controller controller;
    private Channel channel;
    private FileSendingHandler fileSendingHandler;

    Sender(Channel channel, Controller controller) {
        this.channel = channel;
        this.controller = controller;
        fileSendingHandler = new FileSendingHandler();
    }

    public void send(Object message){
        try {
            ChannelFuture future = channel.writeAndFlush(message);
            future.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void actionWithFile(String command, String currentFolder, File[] files){
        RequestCatalog catalog = fileSendingHandler.actionWithFile(channel, command, currentFolder, files);
        showChange(catalog.getCurrentCatalog(), catalog.getCatalog());
    }


    private void showChange(final String currentFolder, final File[] files){
        Platform.runLater(() ->{
            controller.getCurrentDirectory().setText(currentFolder);
            controller.getData().setAll(files);
        });
    }
}
