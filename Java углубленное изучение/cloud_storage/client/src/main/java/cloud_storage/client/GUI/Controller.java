package cloud_storage.client.GUI;

import cloud_storage.client.Client;
import cloud_storage.common.SCM;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;
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
    @FXML
    private Label directoryOnServer;
    @FXML
    private TableView<File> server;
    @FXML
    private TableColumn<File, String> serverName;
    @FXML
    private TableColumn<File, String> serverSize;
    @FXML
    private TableColumn<File, String> serverLastModified;

    private ObservableList<File> data;
    private ObservableList<File> catalogUser;
    private MultipleSelectionModel<File> selectedLocal;
    private MultipleSelectionModel<File> selectedServer;
    private List<File> listSelected;
    private boolean onServer;

    public ObservableList<File> getData() {
        return data;
    }

    public Label getCurrentDirectory() {
        return currentDirectory;
    }

    public TextField getLogin() {
        return login;
    }

    public PasswordField getPass() {
        return pass;
    }

    public Label getDirectoryOnServer() {
        return directoryOnServer;
    }

    public ObservableList<File> getCatalogUser() {
        return catalogUser;
    }

    public void rename(ActionEvent actionEvent) {

    }

    public void copy(ActionEvent actionEvent) {
        listSelected = getSelected();
        if (listSelected == null) return;
        String command = (onServer)? SCM.COPY : String.format("%s %s", SCM.SERVER, SCM.COPY);
        Client.getInstance().sendRequestForActionWithFile(command, listSelected);
    }

    public void relocate(ActionEvent actionEvent) {
    }

    public void delete(ActionEvent actionEvent) {
    }

    public void createFolder(ActionEvent actionEvent) {
    }

    public void update(ActionEvent actionEvent) {
    }


    private List<File> getSelected(){
        if (!selectedLocal.isEmpty()){
            onServer = true;
            return selectedLocal.getSelectedItems();
        }
        if (!selectedServer.isEmpty()){
            onServer = false;
            return selectedServer.getSelectedItems();
        }
        // TODO: 16.07.2018 сообщение о отсутвии выбранных файлов
        System.out.println("Нет выбранных файлов");
        return null;
    }

    public void choiceOfSide(MouseEvent mouseEvent) {
        if (local.isFocused()) selectedServer.clearSelection();
        if (server.isFocused())selectedLocal.clearSelection();
    }

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
        if (Client.getInstance().connect())
            Client.getInstance().sendRequest(String.format("%s %s %s", SCM.AUTH, login.getText(), pass.getText()));

    }

    public void authorization(){
        boolean auth = Client.getInstance().isAuthorized();
        offline.setVisible(!auth);
        offline.setManaged(!auth);
        connected.setVisible(auth);
        connected.setManaged(auth);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Client.getInstance().setController(this);
        data = FXCollections.observableArrayList();
        catalogUser = FXCollections.observableArrayList();
        selectedLocal = local.getSelectionModel();
        selectedServer = server.getSelectionModel();
        serverName.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getName()));
        serverSize.setCellValueFactory(p -> {
            String size = (p.getValue().isFile()) ? String.format("%d байт", p.getValue().length()): "Folder";
            return new ReadOnlyObjectWrapper<>(size);
        });
        serverLastModified.setCellValueFactory(p -> {
            Date dateTime = new Date(p.getValue().lastModified());
            return new ReadOnlyObjectWrapper<>(dateTime.toString());
        });
        server.setItems(catalogUser);
        selectedServer.setSelectionMode(SelectionMode.MULTIPLE);
        localName.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getName()));
        localSize.setCellValueFactory(p -> {
            String size = (p.getValue().isFile()) ? String.format("%d байт", p.getValue().length()): "Folder";
            return new ReadOnlyObjectWrapper<>(size);
        });
        localLastModified.setCellValueFactory(p -> {
            Date dateTime = new Date(p.getValue().lastModified());
            return new ReadOnlyObjectWrapper<>(dateTime.toString());
        });
        local.setItems(data);
        selectedLocal.setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void disconnect(ActionEvent actionEvent){
        if (Client.getInstance().isAuthorized()) {
            Client.getInstance().disconnect();
            authorization();
        }
    }
}
