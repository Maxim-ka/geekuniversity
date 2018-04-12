package onConsol.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {

    private static final int PORT = 8189;
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final String DISCONNECTION = "/end";
    private static final Scanner SCANNER = new Scanner(System.in);
    private static Socket socket;

    public static void main(String[] args) {

        try  {
            socket = new Socket(IP_ADDRESS, PORT);
            DataInputStream in  = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            new Thread(() ->{
                try {
                    boolean transfer = true;
                    while (transfer){
                        String message = SCANNER.nextLine();
                        if (DISCONNECTION.equalsIgnoreCase(message)) {
                            SCANNER.close();
                            transfer = false;
                            System.out.println("Отключение клиента");
                        }
                        if (socket.isClosed()){
                            System.out.println("Сервер не подключен");
                            return;
                        }
                        else {
                            out.writeUTF(message);
                            out.flush();
                        }
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() ->{
                try {
                    boolean communication = true;
                    while (communication) {
                        String message = in.readUTF();
                        if (DISCONNECTION.equalsIgnoreCase(message)){
                            out.writeUTF(message);
                            communication = false;
                            System.out.println("Отключились от сервера");
                        } else System.out.println(message);
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

            }).start();
        }catch (IOException e) {
            System.out.println("А сервер точно включен?");
        }
    }
}
