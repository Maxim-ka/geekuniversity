package onConsol.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class Server extends ServerSocket{
    private static final String DISCONNECTION = "/end";
    private final Scanner scanner = new Scanner(System.in);
    private final Object lock = new Object();
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private volatile boolean communication;
    private volatile boolean work = true;
    private volatile String message;

    Server(int port) throws IOException {
        super(port);
        System.out.println("Север запущен, ожидает подключения");
        new Thread(()->{
            while (work){
                synchronized (lock){
                    message = scanner.nextLine();
                    if (DISCONNECTION.equalsIgnoreCase(message)){
                        work = false;
                        System.out.println("Отключение сервера");
                        scanner.close();
                    }
                }
                if (socket == null || socket.isClosed()) {
                    if (!work) System.exit(0);
                    System.out.println("Клиент не подключен");
                }
            }
        }).start();
    }

    void getConnected(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        System.out.println("Клиент подключился");
        communication = true;
        new Thread(() ->{
            try {
                do{
                    if (message != null){
                        synchronized (lock){
                            out.writeUTF(message);
                            out.flush();
                            message = null;
                        }
                    }
                }while (communication);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    void getMessage(){
        try {
            while (communication) {
                String message = in.readUTF();
                if (DISCONNECTION.equalsIgnoreCase(message)){
                    communication = false;
                    out.writeUTF(message);
                    System.out.println("Клиент отключился");
                }else System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean isContinue(){
        return work;
    }
}
