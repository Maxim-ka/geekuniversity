package cloud_storage.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ReceiverCollectorFile {

    private int countPart;


    public void writeFile(String currentFolder, TransferFile transferFile){
        if (transferFile.getBytes() == null){
            new File(String.format("%s/%s", currentFolder, transferFile.getFile())).mkdir();
            return;
        }
        String nameFile = String.format("%s/%s" ,currentFolder, transferFile.getFile());
        File tempFile;
        ByteBuffer byteBuffer = ByteBuffer.wrap(transferFile.getBytes());
        try(FileOutputStream outputStream = new FileOutputStream(tempFile = new File(nameFile.concat(".temp")), true)) {
            FileChannel fileChannel = outputStream.getChannel();
            while (byteBuffer.hasRemaining()){
                fileChannel.write(byteBuffer);
            }
            fileChannel.close();
            System.out.println(transferFile.getPortion() + "часть файла записанного");
            ++countPart;
            System.out.println(countPart + "счетчик");
            if (countPart == transferFile.getTotal()){
                checkNameFile(nameFile, tempFile);
                countPart = 0;
            }else if (transferFile.getTotal() == transferFile.getPortion()){
                System.out.println("нарушен порядок файлов");
                countPart = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkNameFile(String nameFile, File tempName) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(nameFile);
        int insertIndex = stringBuilder.lastIndexOf(".");
        int numCopy = 0;
        while (new File(nameFile).exists()){
            if (insertIndex == stringBuilder.lastIndexOf(".")) stringBuilder.insert(insertIndex, String.format("(%d)", ++numCopy));
            else stringBuilder.replace(insertIndex, String.format("(%d)", numCopy).length(), String.format("(%d)", ++numCopy));
            nameFile = stringBuilder.toString();
        }
        Files.move(tempName.toPath(), new File(nameFile).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
