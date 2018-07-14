package cloud_storage.client;

import cloud_storage.common.Rule;
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
    private BorderPane connected;
    @FXML
    private Pane offline;

    private ObservableList<File> data;
    private final Client client = new Client(Rule.IP_ADDRESS, Rule.PORT);

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
        if (login.getText().isEmpty() || pass.getText().isEmpty()) {
            // TODO: 12.07.2018 написать окно предупреждения
            return;
        }
        if (client.connect()){
            client.toSendServiceMessage(String.format("%s %s %s", SCM.AUTH, login.getText(), pass.getText()));
            client.getAuthorization();
            if (client.isAuthorized()){
                offline.setVisible(false);
                offline.setManaged(false);
                connected.setVisible(true);
                connected.setManaged(true);
            }// TODO: 14.07.2018 вывод сообщения о не удачной авторизации
        }
        login.clear();
        pass.clear();
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
