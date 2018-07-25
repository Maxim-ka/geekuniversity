package cloud_storage.common;

import io.netty.channel.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileSendingHandler{

    private Path currentDirectory;

    public RequestCatalog actionWithFile(Channel channel, String command, String currentFolder, File[] files){
        currentDirectory = Paths.get(currentFolder);
        for (File file : files) {
            if (file.isFile()) {
                try {
                    switch (command) {
                        case SCM.COPY:
                            readFile(channel, file);
                            break;
                        case SCM.DELETE:
                            Files.delete(file.toPath());
                            break;
                        case SCM.RELOCATE:
                            readFile(channel, file);
                            Files.delete(file.toPath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                continue;
            }
            if (file.isDirectory()) {
                try {
                    Files.walkFileTree(file.toPath(), new FileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                            if (command.startsWith(SCM.COPY) || command.startsWith(SCM.RELOCATE)) {
                                Path relativeDir = currentDirectory.relativize(dir);
                                readFolder(channel, relativeDir.toFile());
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            switch (command) {
                                case SCM.COPY:
                                    readFile(channel, file.toFile());
                                    break;
                                case SCM.DELETE:
                                    Files.delete(file);
                                    break;
                                case SCM.RELOCATE:
                                    readFile(channel, file.toFile());
                                    Files.delete(file);
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                            System.out.println(exc.getCause().toString()); // TODO: 24.07.2018 логирование и сообщения
                            System.out.println(exc.getMessage());
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                            if (command.startsWith(SCM.RELOCATE) || command.startsWith(SCM.DELETE)) {
                                Files.delete(dir);
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new RequestCatalog(SCM.OK, currentFolder, new File(currentFolder).listFiles());
    }

    private File getRelativePathFile(File file){
        return currentDirectory.relativize(file.toPath()).toFile();
    }

    private void readFolder(Channel channel, File file){
        channel.writeAndFlush(new TransferFile(file, 1, 1, null));
    }

    private void readFile(Channel channel,File file){
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            int quotient = (int) (randomAccessFile.length() /  Rule.MAX_NUMBER_TRANSFER_BYTES);
            int totalPart = (randomAccessFile.length() %  Rule.MAX_NUMBER_TRANSFER_BYTES == 0) ? quotient : quotient + 1;
            FileChannel fileChannel = randomAccessFile.getChannel();
            int size = (randomAccessFile.length() >= Rule.MAX_NUMBER_TRANSFER_BYTES) ? Rule.MAX_NUMBER_TRANSFER_BYTES : (int) randomAccessFile.length();
            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            int numOfBytesRead = 0;
            int portion = 0;
            byte[] bytes;
            File relFile = getRelativePathFile(file);
            while (numOfBytesRead != -1){
                numOfBytesRead = fileChannel.read(byteBuffer);
                if (numOfBytesRead == byteBuffer.capacity() || (numOfBytesRead == -1 && byteBuffer.position() != 0)){
                    if (numOfBytesRead != -1) bytes = byteBuffer.array();
                    else {
                        bytes = new byte[byteBuffer.position()];
                        byteBuffer.flip();
                        byteBuffer.get(bytes);
                    }
                    TransferFile transferFile = new TransferFile(relFile, ++portion, totalPart, bytes);
                    ChannelFuture future = channel.writeAndFlush(transferFile);
                    // TODO: 25.07.2018 Проблема передачи больших файлов > Rule.MAX_NUMBER_TRANSFER_BYTES от сервера к клиенту без блокировки
                    System.out.println(transferFile.getPortion());
                    byteBuffer.clear();
                }
            }
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
