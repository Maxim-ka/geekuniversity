package onJavaFX.server;

import onJavaFX.SMC;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.Vector;

class ChatServer extends Thread{

    private static final int TIMEOUT = 3000;
    private static final int LIMIT = 3;
    private int port;
    private final Vector<ClientHandler> clients  = new Vector<>();
    private final AuthService authService = new AuthService();

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
            stopClientHandler();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException | SQLException e){
            System.out.println("Нет авторизации на сервере");
        }finally {
            authService.disconnect();
            System.out.println("выключение сервера");
        }
    }

    private void stopClientHandler(){
        broadcastMsg(SMC.DISCONNECTION);
    }

    synchronized void sendPrivateMessages(String message, ClientHandler clientHandler){
        String[] strings = message.split("\\s+", LIMIT);
        for (ClientHandler client : clients) {
            if (client.getNick().equals(strings[1])){
                client.sendMessage(String.format("%s: %s;", clientHandler.getNick(), strings[2]));
                clientHandler.sendMessage(String.format("%s: @%s (%s);",clientHandler.getNick(), strings[1], strings[2]));
                return;
            }
        }
        clientHandler.sendMessage(String.format("%s %s", SMC.NO, strings[1]));
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
