package cloud_storage.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ClientHandler implements Runnable {

    private SelectionKey key;
    private Selector selector;
    private SocketChannel socketChannel;
    private ByteBuffer byteBuffer;

    public ClientHandler(SelectionKey key, Selector selector) throws IOException {
        this.key = key;
        this.selector = selector;
        socketChannel = ((ServerSocketChannel) key.channel()).accept();
        String address = (new StringBuilder(socketChannel.socket().getInetAddress().toString())).append(":").append(socketChannel.socket().getPort()).toString();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, address);
        byteBuffer = ByteBuffer.allocate(256);
    }

    @Override
    public void run() {
        if (key.isReadable()){

        }


    }
}
