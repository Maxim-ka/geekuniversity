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
import javafx.stage.Modality;
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
                out.writeUTF(textField.getText());
                out.flush();
                textField.clear();
                textField.requestFocus();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearMessage() {
        if (textArea.getText().isEmpty()) return;
        Optional<ButtonType> result = new Caution("Вы точно хотите удалить все записи в чате?").showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.YES)) {
            textArea.clear();
            textField.requestFocus();
        }
    }

    @FXML
    private void sendAuthMsg(){
        if (loginField.getText().isEmpty() || passField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING,"Поля: логин и/или пароль не заполнены");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            return;
        }
        if(isConnect()){
            try {
                out.writeUTF(String.format("/auth %s %s", loginField.getText(), passField.getText()));
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
                if (socket.isClosed())outputToLabel(NO_COMMUNICATION);
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
                    String[] strings = string.split("\\s+");
                    switch (strings[0].toLowerCase()){
                        case SMC.OK:
                            authorize(true);
                            outputToLabel(strings[1]);
                            break;
                        case SMC.DISCONNECTION:
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
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.initModality(Modality.APPLICATION_MODAL);
                                alert.setHeaderText("Сообщение не было доставлено.");
                                alert.setContentText(String.format("Отсутствие адресата %s", strings[1]));
                                alert.showAndWait();
                            });
                            break;
                        default:
                            if (authorized) textArea.appendText(string + "\n");
                    }
                }while (authorized);
            }catch (IOException e){
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