package cloud_storage.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ActionOnFile {

    public void writeFile(String currentFolder, TransferFile transferFile){
        ByteBuffer byteBuffer = ByteBuffer.wrap(transferFile.getBytes());
        try(FileOutputStream outputStream = new FileOutputStream(new File(String.format("%s/%s" ,currentFolder, transferFile.getFile().getName())), true)) {
            FileChannel fileChannel = outputStream.getChannel();
            while (byteBuffer.hasRemaining()){
                fileChannel.write(byteBuffer);
            }
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
