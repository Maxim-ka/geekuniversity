package onSwing;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame implements ActionListener{

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String TITLE = "ЧАТ КЛИЕНТ";
    private final JTextArea textArea = new JTextArea();
    private final JButton buttonClear = new JButton("Очистить");
    private final JTextField textField = new JTextField();
    private final JButton buttonSend = new JButton("Отправить");
    private final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

    private ClientGUI(){
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int answer = showConfirmDialog("Выход", "Вы точно хотите выйти из чата?");
                if (answer == JOptionPane.OK_OPTION) System.exit(0);
            }
        });

        setMinimumSize(new Dimension(WIDTH,HEIGHT));
        setLocationRelativeTo(null);
        setTitle(TITLE);
        setResizable(false);

        textArea.setEditable(false);
        textArea.setBackground(new Color(255, 250, 205));
        textArea.setFont(font.deriveFont(Font.ITALIC));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollLog = new JScrollPane(textArea);
        add(scrollLog, BorderLayout.CENTER);

        GuidanceOn guidanceOn = new GuidanceOn();
        buttonClear.addMouseListener(guidanceOn);
        buttonClear.addFocusListener(guidanceOn);
        buttonClear.addActionListener(this);
        textField.addActionListener(this);
        textField.addFocusListener(guidanceOn);
        buttonSend.addActionListener(this);
        buttonSend.addMouseListener(guidanceOn);
        buttonSend.addFocusListener(guidanceOn);

        buttonClear.setFont(font);
        textField.setFont(font);
        buttonSend.setFont(font);

        JPanel bottonPanel = new JPanel(new BorderLayout());
        bottonPanel.add(buttonClear, BorderLayout.WEST);
        bottonPanel.add(textField, BorderLayout.CENTER);
        bottonPanel.add(buttonSend, BorderLayout.EAST);
        add(bottonPanel, BorderLayout.SOUTH);

        JList<String> listUsers = new JList<>();
        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        listUsers.setModel(defaultListModel);
        listUsers.setFont(font);
        listUsers.setFixedCellWidth(this.getWidth() / 4);
        listUsers.setAutoscrolls(true);
        JScrollPane scrollUsers = new JScrollPane(listUsers);
        add(scrollUsers, BorderLayout.EAST);

        pack();
        textField.requestFocusInWindow();
        setVisible(true);
    }

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object objEvent = e.getSource();
        if (objEvent == textField || objEvent == buttonSend) sendMessage();
        if (objEvent == buttonClear) clearMessage();
    }

    private void sendMessage(){
        if (textField.getText().isEmpty()) return;
        textArea.append(textField.getText().concat("\n"));
        textField.setText(null);
        textField.grabFocus();
    }

    private void clearMessage(){
        if (textArea.getText().isEmpty()) return;
        int answer = showConfirmDialog("Удаление", "Вы точно хотите удалить все записи в чате?");
        if (answer == JOptionPane.OK_OPTION) {
            textArea.setText(null);
            textField.grabFocus();
        }
    }

    private int showConfirmDialog(String title, String question){
        return JOptionPane.showConfirmDialog(null, question, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    private class GuidanceOn extends MouseInputAdapter implements FocusListener{

        private static final float SIZE_FONT = 22f;
        private final Color buttonDefaultBackground = buttonSend.getBackground();
        private final Color green = new Color(84, 255, 159);
        private final Color red = new Color(255, 69, 0);
        private final Color yellow = new Color(252, 255, 189);

        @Override
        public void mouseEntered(MouseEvent e) {
            Object objEvent = e.getSource();
            if (objEvent == buttonSend) {
                buttonSend.setBackground(green);
                buttonSend.setFont(font.deriveFont(Font.BOLD, SIZE_FONT));
            }
            if (objEvent == buttonClear){
                buttonClear.setBackground(red);
                buttonClear.setFont(font.deriveFont(Font.BOLD, SIZE_FONT));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Object objEvent = e.getSource();
            if (objEvent == buttonSend) {
                buttonSend.setBackground(buttonDefaultBackground);
                buttonSend.setFont(font);
            }
            if (objEvent == buttonClear){
                buttonClear.setBackground(buttonDefaultBackground);
                buttonClear.setFont(font);
            }
        }

        @Override
        public void focusGained(FocusEvent e) {
            Object objEvent = e.getSource();
            if (objEvent == buttonSend){
                buttonSend.setBackground(green);
                buttonSend.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) sendMessage();
                    }
                });
            }
            if (objEvent == buttonClear){
                buttonClear.setBackground(red);
                buttonClear.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) clearMessage();
                    }
                });
            }
            if (objEvent == textField) textField.setBackground(yellow);
        }

        @Override
        public void focusLost(FocusEvent e) {
            Object objEvent = e.getSource();
            if (objEvent == buttonSend) buttonSend.setBackground(buttonDefaultBackground);
            if (objEvent == buttonClear) buttonClear.setBackground(buttonDefaultBackground);
            if (objEvent == textField) textField.setBackground(Color.WHITE);
        }
    }
}
