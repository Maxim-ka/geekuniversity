package cloud_storage.common;

import io.netty.channel.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Sender {

    private Channel channel;

    public Sender(Channel channel) {
        this.channel = channel;
    }

    public void send(Object message){
        try {
            ChannelFuture future = channel.writeAndFlush(message);
            future.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void actionWithFile(String command, File[] files){
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()){
                readFile(command, files[i]);
                continue;
            }
            if (files[i].isDirectory()){

            }
        }
    }

    private void readFile(String command, File file){
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            int quotient = (int) (randomAccessFile.length() /  Rule.MAX_NUMBER_TRANSFER_BYTES);
            int totalPart = (randomAccessFile.length() %  Rule.MAX_NUMBER_TRANSFER_BYTES == 0) ? quotient : quotient + 1;
            FileChannel fileChannel = randomAccessFile.getChannel();
            int size = (randomAccessFile.length() >= Rule.MAX_NUMBER_TRANSFER_BYTES) ? Rule.MAX_NUMBER_TRANSFER_BYTES : (int) randomAccessFile.length();
            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            int numOfBytesRead = 0;
            int portion = 0;
            byte[] bytes;
            while (numOfBytesRead != -1){
                numOfBytesRead = fileChannel.read(byteBuffer);
                if (numOfBytesRead == byteBuffer.capacity() || (numOfBytesRead == -1 && byteBuffer.position() != 0)){
                    if (numOfBytesRead != -1) bytes = byteBuffer.array();
                    else {
                        bytes = new byte[byteBuffer.position()];
                        byteBuffer.flip();
                        byteBuffer.get(bytes);
                    }
                    send(new TransferFile(command, file, ++portion, totalPart, bytes));
                    byteBuffer.clear();
                }
            }
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
