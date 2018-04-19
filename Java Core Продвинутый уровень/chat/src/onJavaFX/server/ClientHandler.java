package onJavaFX.server;
import onJavaFX.SMC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ClientHandler extends Thread{
    private ChatServer server;
    private Socket socket;
    private DataInputStream in ;
    private DataOutputStream out;
    private String nick;
    private boolean connection = true;

    public String getNick() {
        return nick;
    }

    @Override
    public void run() {
        String msg;
        try {
            do{
                msg = in.readUTF();
                if (msg.startsWith(SMC.PREFIX)){
                    if (msg.equalsIgnoreCase(SMC.DISCONNECTION)) connection = false;
                    if (msg.startsWith(SMC.AUTH)) msg = confirmAuthorization(msg);
                    if (!connection || msg.startsWith(SMC.OK))sendMessage(msg);
                    if (msg.contains(SMC.W)) server.sendPrivateMessages(msg, this);
                }else server.broadcastMsg(String.format("%s: %s;",nick, msg));
            }while (connection);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            server.unsubscribe(this);
            try {
                in.close();
                out.flush();
                out.close();
                socket.close();
                System.out.println("Клиент отключился");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    ClientHandler(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try{
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        start();
    }

    void sendMessage(String message){
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String confirmAuthorization(String string){
        String[] strings = string.split("\\s+");
        nick = server.getAuthService().getNick(strings[1], strings[2]);
        if (nick != null){
            if (server.isBusyNick(nick)){
                connection = false;
                return SMC.REPETITION;
            }
            server.subscribe(this);
            connection = true;
            return String.format("%s %s", SMC.OK, nick);
        }else{
            connection = false;
            return SMC.INVALID;
        }
    }
}
