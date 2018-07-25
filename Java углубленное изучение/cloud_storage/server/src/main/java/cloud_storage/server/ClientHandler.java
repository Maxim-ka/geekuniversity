package cloud_storage.server;

import cloud_storage.common.*;
import io.netty.channel.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Vector;

class ClientHandler extends ChannelInboundHandlerAdapter{

    private final ActionOnFile action = new ActionOnFile();
    private final Vector<String> listOfOpenChannels;
    private String rootUserDirectory;
    private Path currentDirectory;

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
                action.writeFile(rootUserDirectory, file);
                // TODO: 20.07.2018 Отправка ответа пока по последней части файла.
                if(file.getPortion() == file.getTotal()){
                    ctx.writeAndFlush(new RequestCatalog(SCM.OK, rootUserDirectory, new File(rootUserDirectory).listFiles()));
                }
                return;
        }
        if (msg instanceof RequestCatalog){  // TODO: 25.07.2018 Проблема передачи больших файлов > Rule.MAX_NUMBER_TRANSFER_BYTES от сервера к клиенту
            RequestCatalog request = (RequestCatalog)msg;
            actionWithFile(ctx, request.getCommand(), request.getCurrentCatalog(), request.getCatalog());
        }
    }

    public void actionWithFile(ChannelHandlerContext context, String command, String currentFolder, File[] files){
        currentDirectory = Paths.get(currentFolder);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()){
                readFile(context,command, files[i]);
                continue;
            }
            if (files[i].isDirectory()){
                try {
                    Files.walkFileTree(files[i].toPath(), new FileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                            Path relativeDir = currentDirectory.relativize(dir);
                            readFolder(context, command, relativeDir.toFile());
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            readFile(context, command, file.toFile());
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

    private void readFolder(ChannelHandlerContext context, String command, File file){
        context.writeAndFlush(new TransferFile(command, file, 0, 0, null));
    }

    private void readFile(ChannelHandlerContext context, String command, File file){
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
                    TransferFile transferFile = new TransferFile(command, relFile, ++portion, totalPart, bytes);
                    ChannelFuture future = context.writeAndFlush(transferFile);
                    System.out.println(transferFile);
                    System.out.println(future.isVoid() + " isVoid()");
                    System.out.println(future.isDone() + " isDone()");
                    System.out.println(future.isSuccess() + " isSuccess()");
                    byteBuffer.clear();
                }
            }
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
        System.out.println(cause.toString());
    }
}
