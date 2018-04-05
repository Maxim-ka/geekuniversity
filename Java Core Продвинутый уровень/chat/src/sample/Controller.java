package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Optional;

import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.YES;

public class Controller {
    private final Background defaultBackground = new Background(new BackgroundFill(Color.WHITE, null, null));
    private final Color green = Color.rgb(84, 255, 159);
    private final Color red = Color.rgb(255, 69, 0);
    private final Color yellow = Color.rgb(252, 255, 189);
    private final Background greenBackground = new Background(new BackgroundFill(green, null, null));
    private final Background redBackground = new Background(new BackgroundFill(red, null, null));
    private final Background yellowBackground = new Background(new BackgroundFill(yellow, null, null));

    @FXML
    private Button buttonSend;
    @FXML
    private Button buttonClear;
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;

    @FXML
    private void sendMessage(){
        if (textField.getText().isEmpty())return;
        textArea.appendText(textField.getText() + "\n");
        textField.clear();
        textField.requestFocus();
    }
    @FXML
    private void clearMessage(){
        if (textArea.getText().isEmpty()) return;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы точно хотите удалить все записи в чате?", YES, CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.YES)) {
            textArea.clear();
            textField.requestFocus();
        }
    }



    public void hoverButtonSend() {
        buttonSend.setBackground(greenBackground);
    }

    public void hoverButtonClear() {
        buttonClear.setBackground(redBackground);
    }

    public void leaveButtonSend() {
        buttonSend.setBackground(defaultBackground);
    }
    public void leaveButtonClear(){
        buttonClear.setBackground(defaultBackground);
    }
}
