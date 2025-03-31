package vi.al.ro.controller.modal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import vi.al.ro.service.db.DataBaseService;

import java.sql.SQLException;

@Log4j2
public class KeyStoreSaverController {

    @FXML
    private TextField tfAlias;

    @FXML
    private TextField tfPassword;

    @FXML
    private TextField tfPath;

    @FXML
    void onSaveClick(ActionEvent event) {
        try {
            DataBaseService.getInstance().insert(tfAlias.getText(), tfPassword.getText(), tfPath.getText());
            log.info("Save in DB!");
        } catch (SQLException e) {
            log.error("", e);
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
