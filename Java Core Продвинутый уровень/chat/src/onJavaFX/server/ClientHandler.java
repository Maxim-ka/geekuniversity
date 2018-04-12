package onJavaFX.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ClientHandler {
    private static final String ECHO = "echo: ";
    private static final String DISCONNECTION = "/end";
    private Socket socket;

    ClientHandler(Socket socket) {
        this.socket = socket;
        try{
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            new Thread(()->{
                String msg;
                try {
                    do{
                        msg = in.readUTF();
                        String message = (msg.equalsIgnoreCase(DISCONNECTION))? msg : ECHO.concat(msg);
                        out.writeUTF(message);
                        out.flush();
                    }while (!DISCONNECTION.equalsIgnoreCase(msg));
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    try {
                        in.close();
                        out.flush();
                        out.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
