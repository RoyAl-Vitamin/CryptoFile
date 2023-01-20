package vi.al.ro.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vi.al.ro.controller.modal.KeyStoreSaverController;
import vi.al.ro.model.fx.KeyStoreModel;
import vi.al.ro.service.DataBaseService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class KeyStoreController implements Initializable {

    private static final Logger log = LogManager.getLogger(KeyStoreController.class);

    @FXML
    private TableColumn<KeyStoreModel, String> tcName;

    @FXML
    private TableColumn<KeyStoreModel, String> tcNeedPassword;

    @FXML
    private TableView<KeyStoreModel> tvData;

    private ObservableList<KeyStoreModel> keyStoreModels = FXCollections.observableArrayList();

    @FXML
    void onAddKeyStoreClick(ActionEvent event) {
        final Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(KeyStoreSaverController.class.getResource("fxmlModalKeyStoreSaver.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            log.error("", e);
            return;
        }
        stage.setScene(scene);
        stage.setTitle("Add KeyStore");
        stage.initOwner(parentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        log.info("NEED UPDATE!");
        try {
            keyStoreModels.addAll(DataBaseService.getInstance().getAll().stream().map(KeyStoreModel::new).collect(Collectors.toList()));
//            studentsModels = FXCollections.observableArrayList(DataBaseService.getAll().stream().map(KeyStoreModel::new).collect(Collectors.toList()));
        } catch (SQLException e) {
            log.error("", e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tcNeedPassword.setCellValueFactory(new PropertyValueFactory<>("NeedPassword"));
        try {
            keyStoreModels = FXCollections.observableArrayList(DataBaseService.getInstance().getAll().stream().map(KeyStoreModel::new).collect(Collectors.toList()));
        } catch (SQLException e) {
            log.error("", e);
        }
        tvData.setItems(keyStoreModels);
    }
}
