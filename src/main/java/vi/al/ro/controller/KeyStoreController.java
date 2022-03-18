package vi.al.ro.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vi.al.ro.controller.modal.KeyStoreSaverController;

import java.io.IOException;

public class KeyStoreController {

    private static final Logger log = LogManager.getLogger(KeyStoreController.class);

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
    }
}
