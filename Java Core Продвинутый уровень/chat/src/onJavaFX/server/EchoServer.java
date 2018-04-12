package onJavaFX.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    private static final int PORT = 8189;


    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true){
                Socket socket = serverSocket.accept();
                new ClientHandler(socket);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
