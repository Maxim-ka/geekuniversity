package cloud_storage.client;

import cloud_storage.common.SCM;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    private static final int PORT = 8189;
    private static final String IP_ADDRESS = "127.0.0.1";
    private final StringBuilder stringBuilder = new StringBuilder();
    private SocketChannel socketChannel;
    private ByteBuffer byteBuffer;
    private boolean authorized;

    Client(){
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            byteBuffer = ByteBuffer.allocate(256);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void connect(){
        try {
            if (socketChannel.isOpen()) socketChannel.connect(new InetSocketAddress(IP_ADDRESS, PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return socketChannel.isConnected();
    }

    public void toSendServiceMessage(String msg){
        try {
            byteBuffer.clear();
            byteBuffer.put(msg.getBytes());
            while (byteBuffer.hasRemaining()){
                socketChannel.write(byteBuffer);
            }
            byteBuffer.rewind();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean confirmAuthorization(){
        try {
            if (getAnswer().equals(SCM.OK)){
                authorized = true;
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getAnswer() throws IOException {
        stringBuilder.delete(0, stringBuilder.length());
        byteBuffer.clear();
        int read = 0;
        while ((read = socketChannel.read(byteBuffer)) > 0) {
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.limit()];
            byteBuffer.get(bytes);
            stringBuilder.append(new String(bytes));
            byteBuffer.clear();
        }
        return stringBuilder.toString();
    }

    public void disconnect(){
//        if (socketThread.isAlive() || !socketThread.isInterrupted()) socketThread.interrupt();
//        sendMessage(SMC.DISCONNECTION);
//        chat.setExit(true);
    }

    public void closeSocket(){
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
