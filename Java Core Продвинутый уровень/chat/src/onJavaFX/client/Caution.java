package onJavaFX.client;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.YES;

class Caution extends Alert {

    Caution(String contentText) {
        super(Alert.AlertType.CONFIRMATION, contentText, YES, CANCEL);
        setHeaderText(null);
        setResizable(false);
        setTitle("ВНИМАНИЕ!");
        initModality(Modality.APPLICATION_MODAL);
    }
}
