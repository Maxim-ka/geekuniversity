package onConsol.server;

import java.io.IOException;
import java.net.Socket;

public class MainServer {

    private static final int PORT = 8189;


    public static void main(String[] args) {

        try (Server server = new Server(PORT)){
            do {
                try (Socket socket = server.accept()) {
                    server.getConnected(socket);
                    server.getMessage();
                } catch (IOException e) {
                    System.out.println("Клиент отвалился");
                }
            }while (server.isContinue());
        } catch (IOException e) {
            System.out.println("сервер сдох");
        }
    }
}
