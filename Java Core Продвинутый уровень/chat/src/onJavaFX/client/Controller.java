package onJavaFX.client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.YES;

public class Controller implements Initializable{


    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;

    private static final String DISCONNECTION = "/end";
    private static final int PORT = 8189;
    private static final String IP_ADDRESS = "127.0.0.1";
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    @FXML
    private void sendMessage() {
        if (textField.getText().isEmpty()) return;
        try {
            if (socket.isClosed()) noCommunication();
            else{
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы точно хотите удалить все записи в чате?", YES, CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.YES)) {
            textArea.clear();
            textField.requestFocus();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread t = new Thread(() -> {
                try {
                    String string;
                    do{
                        string = in.readUTF();
                        textArea.appendText(string + "\n");
                    }while (!string.equalsIgnoreCase(DISCONNECTION));
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.setDaemon(true);
            t.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void noCommunication(){
        textField.setAlignment(Pos.CENTER);
        textField.setText("Нет связи с сервером");
        textField.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(20), textField.getInsets())));
    }
}