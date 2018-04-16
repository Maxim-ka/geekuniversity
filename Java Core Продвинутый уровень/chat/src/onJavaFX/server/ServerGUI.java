package onJavaFX.server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame implements ActionListener{

    private static final int WIDTH = 250;
    private static final int HEIGHT = 125;
    private static final int POS_X = 1500;
    private static final int POS_Y = 200;
    private static final int ROWS = 1;
    private static final int COLS = 2;
    private static final String START = "Start";
    private static final String STOP = "Stop";
    private static final String SERVER = "Server";
    private static final int PORT = 8189;

    private final JButton buttonStart = new JButton(START);
    private final JButton buttonStop = new JButton(STOP);

    private ChatServer chatServer;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerGUI();
            }
        });
    }

    private ServerGUI(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocation(POS_X, POS_Y);
        setTitle(SERVER);
        setResizable(false);
        setAlwaysOnTop(true);

        setLayout(new GridLayout(ROWS, COLS));
        buttonStart.addActionListener(this);
        buttonStop.addActionListener(this);
        add(buttonStart);
        add(buttonStop);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object objEvent = e.getSource();
        if (objEvent == buttonStart) {
            if (chatServer != null && chatServer.isAlive()) System.out.println("сервер уже работает");
            else chatServer = new ChatServer(PORT);
        }
        if (objEvent == buttonStop){
            if (chatServer == null || !chatServer.isAlive())System.out.println("сервер не запущен");
            else chatServer.interrupt();
        }
    }
}
