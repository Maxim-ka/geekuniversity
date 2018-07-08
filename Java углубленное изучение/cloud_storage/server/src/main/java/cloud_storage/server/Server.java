package cloud_storage.server;

import cloud_storage.server.ClientHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server implements Runnable {

    private final int port;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private SelectionKey key;

    public Server(int port) throws IOException {
        this.port = port;
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        try {
//            Iterator<SelectionKey> iter;
//            SelectionKey key;
            while (serverSocketChannel.isOpen()) {
                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();
                    if (key.isAcceptable()) {
                        new Thread(new ClientHandler(key, selector)).start();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
