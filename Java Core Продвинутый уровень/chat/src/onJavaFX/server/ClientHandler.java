package onJavaFX.server;
import onJavaFX.SMC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ClientHandler {
    private ChatServer server;
    private Socket socket;
    private DataInputStream in ;
    private DataOutputStream out;
    private String nick;
    private boolean connection = true;

    public String getNick() {
        return nick;
    }

    ClientHandler(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try{
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(()->{
                String msg;
                try {
                    do{
                        msg = in.readUTF();
                        if (server.isInterrupted() || !server.isAlive()) msg = SMC.DISCONNECTION;
                        if (msg.equalsIgnoreCase(SMC.DISCONNECTION)) connection = false;
                        if (msg.startsWith("/auth ")) msg = confirmAuthorization(msg);
                        if (!connection || msg.startsWith(SMC.OK))sendMessage(msg);
                        else if (msg.contains(SMC.W)) sendPrivateMessages(msg);
                        else server.broadcastMsg(String.format("%s: %s;",nick, msg));
                    }while (connection);
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    if (server.isAlive()) server.unsubscribe(this);
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
            }).start();
        }catch (IOException e) {
            e.printStackTrace();
        }
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

    private void sendPrivateMessages(String message){
        String[] strings = message.trim().split("\\s+");
        String msg = server.sendToSpecificClient(strings, this);
        if (msg != null) sendMessage(String.format("%s: @%s (%s);", nick, strings[1], msg));
        else  sendMessage(String.format("%s %s", SMC.NO, strings[1]));
    }
}
