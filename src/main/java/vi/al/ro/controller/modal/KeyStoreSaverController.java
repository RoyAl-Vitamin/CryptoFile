package vi.al.ro.controller.modal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vi.al.ro.model.KeyStoreEntity;
import vi.al.ro.service.DataBaseService;

import java.sql.SQLException;

public class KeyStoreSaverController {

    private static final Logger log = LogManager.getLogger(KeyStoreSaverController.class);

    @FXML
    private TextField tfAlias;

    @FXML
    private TextField tfPassword;

    @FXML
    private TextField tfPath;

    @FXML
    void onSaveClick(ActionEvent event) {
        KeyStoreEntity entity = new KeyStoreEntity();
        entity.setAlias(tfAlias.getText());
        entity.setPassword(tfPassword.getText());
        entity.setPathToFile(tfPath.getText());
        try {
            DataBaseService.save(entity);
            log.info("Save in DB!");
        } catch (SQLException e) {
            log.error("", e);
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
