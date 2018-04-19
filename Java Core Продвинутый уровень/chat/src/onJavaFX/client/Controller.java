package onJavaFX.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import onJavaFX.SMC;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Optional;

class Controller {
    private static final String NO_COMMUNICATION = "Отсутствует связь с сервером";
    private static final int LIMIT = 2;
    @FXML
    private Label label;
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passField;

    private static final int PORT = 8189;
    private static final String IP_ADDRESS = "127.0.0.1";
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private volatile boolean authorized;
    private boolean exit;

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    @FXML
    private void sendMessage() {
        if (textField.getText().isEmpty()) return;
        try {
            if (socket.isClosed())  authorize(false);
            else {
                String string = textField.getText();
                if (string.equalsIgnoreCase(SMC.DISCONNECTION)) exit = true;
                out.writeUTF(string);
                out.flush();
                textField.clear();
                textField.requestFocus();
            }
        }catch (IOException e) {
            try {
                socket.close();
                authorize(false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @FXML
    private void clearMessage() {
        if (textArea.getText().isEmpty()) return;
        Optional<ButtonType> result = new Caution(Alert.AlertType.CONFIRMATION,
                "Вы точно хотите удалить все записи в чате?").showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            textArea.clear();
            textField.requestFocus();
        }
    }

    @FXML
    private void sendAuthMsg(){
        if (loginField.getText().isEmpty() || passField.getText().isEmpty()) {
            new Caution(Alert.AlertType.WARNING,
                    "Поля: логин и/или пароль не заполнены").showAndWait();
            return;
        }
        if(isConnect()){
            try {
                out.writeUTF(String.format("%s %s %s", SMC.AUTH, loginField.getText(), passField.getText()));
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                loginField.clear();
                passField.clear();
            }
        }
    }

    private void authorize(boolean authorized){
        this.authorized = authorized;
        Platform.runLater(() ->{
            try{
                String path;
                Stage stage = (Stage) label.getScene().getWindow();
                if (authorized){
                    path = "chat/chat.fxml";
                }else {
                    path = "login/login.fxml";
                }
                FXMLLoader loader = new  FXMLLoader(getClass().getResource(path));
                loader.setController(this);
                Parent root = loader.load();
                stage.setScene(new Scene(root));
                stage.centerOnScreen();
                if (socket.isClosed() && !exit)outputToLabel(NO_COMMUNICATION);
                stage.show();
            }catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean isConnect(){
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new ClientSocketThread().start();
        }catch (ConnectException e){
            outputToLabel(NO_COMMUNICATION);
            return false;
        }catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }


    private class ClientSocketThread extends Thread{

        @Override
        public void run() {
            try {
                String string;
                do{
                    string = in.readUTF();
                    String[] strings = string.split("\\s+", LIMIT);
                    if (strings[0].startsWith(SMC.PREFIX)){
                        switch (strings[0].toLowerCase()){
                            case SMC.OK:
                                authorize(true);
                                outputToLabel(strings[1]);
                                break;
                            case SMC.DISCONNECTION:
                                out.writeUTF(SMC.DISCONNECTION);
                                out.flush();
                                authorize(false);
                                break;
                            case SMC.INVALID:
                                outputToLabel("Неверный логин и/или пароль");
                                break;
                            case SMC.REPETITION:
                                outputToLabel("Учетная запись уже используется");
                                break;
                            case SMC.NO:
                                Platform.runLater(()->{
                                    new Caution(Alert.AlertType.ERROR,
                                        String.format("Сообщение не было доставлено. Отсутствие адресата %s",
                                                    strings[1])).showAndWait();
                                });
                                break;
                            default:
                                new Caution(Alert.AlertType.ERROR,
                                        String.format("Неизвестное сообщение: %s", string)).showAndWait();
                        }
                    }else if (authorized) textArea.appendText(string + "\n");
                }while (authorized);
            }catch (IOException e){
                authorize(false);
                e.printStackTrace();
            }finally {
                try {
                    socket.close();
                    System.out.println("все закончилось");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void outputToLabel(String string){
        Platform.runLater(() ->{
            label.setText(string);
            if (!authorized) label.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        });
    }
}