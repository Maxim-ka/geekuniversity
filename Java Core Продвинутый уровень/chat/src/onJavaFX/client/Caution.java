package onJavaFX.client;

import javafx.scene.control.Alert;
import javafx.stage.Modality;

class Caution extends Alert {

    Caution(Alert.AlertType alertType, String contentText) {
        super(alertType);
        setHeaderText(null);
        setContentText(contentText);
        setResizable(false);
        setTitle("ВНИМАНИЕ!");
        initModality(Modality.APPLICATION_MODAL);
    }
}
