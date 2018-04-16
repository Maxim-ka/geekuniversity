package onJavaFX.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.Vector;

class ChatServer extends Thread{

    private static final int TIMEOUT = 3000;
    private int port;
    private final Vector<ClientHandler> clients  = new Vector<>();;
    private final AuthService authService = new AuthService();;

    ChatServer(int port) {
        this.port = port;
        start();
    }

    public AuthService getAuthService() {
        return authService;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            authService.connect();
            serverSocket.setSoTimeout(TIMEOUT);
            System.out.println("сервер включен");
            while (!isInterrupted()){
                Socket socket;
                try {
                    socket = serverSocket.accept();
                }catch (SocketTimeoutException e){
                    continue;
                }
                new ClientHandler(this, socket);
                System.out.println("Клиент подключился");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException | SQLException e){
            System.out.println("Нет авторизации на сервере");
        }finally {
            authService.disconnect();
            System.out.println("выключение сервера");
        }
    }

    synchronized String sendToSpecificClient(String[] strings, ClientHandler clientHandler){
        for (ClientHandler client : clients) {
            if (client.getNick().equals(strings[1])){
                StringBuilder  stringBuilder = new StringBuilder();
                for (int i = 2; i < strings.length; i++) {
                     stringBuilder.append(strings[i]).append(" ");
                }
                String message = stringBuilder.toString();
                client.sendMessage(String.format("%s: %s;", clientHandler.getNick(), message));
                return message;
            }
        }
        return null;
    }

    synchronized boolean isBusyNick(String nick){
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getNick().equals(nick)) return true;
        }
        return false;
    }

    synchronized void broadcastMsg(String message){
        for (ClientHandler clientHandler : clients) {
            clientHandler.sendMessage(message);
        }
    }

    synchronized void subscribe(ClientHandler clientHandler){
        clients.add(clientHandler);
    }

    synchronized void unsubscribe(ClientHandler clientHandler){
        clients.remove(clientHandler);
    }
}
