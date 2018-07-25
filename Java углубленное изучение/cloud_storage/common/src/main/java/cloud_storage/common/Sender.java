package cloud_storage.common;

import io.netty.channel.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Sender{

    private Channel channel;
    private Path currentDirectory;

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

    public void actionWithFile(String command, String currentFolder, File[] files){
        currentDirectory = Paths.get(currentFolder);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()){
                readFile(command, files[i]);
                continue;
            }
            if (files[i].isDirectory()){
                try {
                    Files.walkFileTree(files[i].toPath(), new FileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                            Path relativeDir = currentDirectory.relativize(dir);
                            readFolder(command, relativeDir.toFile());
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                            readFile(command, file.toFile());
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
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private File getRelativePathFile(File file){
        return currentDirectory.relativize(file.toPath()).toFile();
    }

    private void readFolder(String command, File file){
        send(new TransferFile(command, file, 0, 0, null));
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
                    send(new TransferFile(command, relFile, ++portion, totalPart, bytes));
                    byteBuffer.clear();
                }
            }
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
