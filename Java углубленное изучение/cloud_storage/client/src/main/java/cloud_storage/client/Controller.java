package cloud_storage.client;

import cloud_storage.common.SCM;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private MenuBar menu;
    @FXML
    private Label currentDirectory;
    @FXML
    private TableView<File> local;
    @FXML
    private TableColumn<File, String> localName;
    @FXML
    private TableColumn<File, String> localSize;
    @FXML
    private TableColumn<File, String> localLastModified;
    @FXML
    private TextField login;
    @FXML
    private PasswordField pass;
    @FXML
    private BorderPane server;
    @FXML
    private Pane connection;

    private ObservableList<File> data;
    private final Client client = new Client();

    public void openFolder(){
        Stage stage = (Stage) menu.getScene().getWindow();
        DirectoryChooser directory = new DirectoryChooser();
        directory.setInitialDirectory(new File("."));
        File catalog = directory.showDialog(stage);
        if (catalog.isDirectory()){
            currentDirectory.setText(catalog.getAbsolutePath());
            data.setAll(catalog.listFiles());
        }
    }

    public void authorize(ActionEvent actionEvent) {
        if (login.getText().isEmpty() || pass.getText().isEmpty()) return;
        client.connect();
        if (client.isConnected()){
            client.toSendServiceMessage(String.format("%s %s %s", SCM.AUTH, login.getText(), pass.getText()));
            login.clear();
            pass.clear();
            if (client.confirmAuthorization()) {
                connection.setVisible(false);
                connection.setManaged(false);
                server.setVisible(true);
                server.setManaged(true);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = FXCollections.observableArrayList();
        localName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<File, String> p) {
                return new ReadOnlyObjectWrapper<>(p.getValue().getName());
            }
        });
        localSize.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<File, String> p) {
                String size = (p.getValue().isFile()) ? String.format("%d байт", p.getValue().length()): "Folder";
                return new ReadOnlyObjectWrapper<>(size);
            }
        });
        localLastModified.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<File, String> p) {
                Date dateTime = new Date(p.getValue().lastModified());
                return new ReadOnlyObjectWrapper<>(dateTime.toString());
            }
        });
        local.setItems(data);

    }
}
