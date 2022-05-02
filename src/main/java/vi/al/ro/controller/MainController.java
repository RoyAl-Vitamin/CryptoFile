package vi.al.ro.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final Logger log = LogManager.getLogger(MainController.class);

    @FXML
    private TabPane tabPane;

    private static final Map<String, String> TAB_MAP = Map.of("Encrypt", "fxmlEncrypt.fxml", "Decrypt", "fxmlDecrypt.fxml", "Key Store", "fxmlKeyStore.fxml");

    @Override
    public void initialize(URL url1, ResourceBundle resourceBundle) {
        // It is important to call it before adding ChangeListener to the tabPane to avoid NPE and
        // to be able to fire the manual selection event below. Otherwise, the 1st tab will be selected
        // with empty content.
        tabPane.getSelectionModel().clearSelection();

        // Add only tabs dynamically but not their content
        for (Map.Entry<String, String> entry : TAB_MAP.entrySet()) {
            tabPane.getTabs().add(new Tab(entry.getKey()));
        }

        // Add Tab ChangeListener
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getContent() == null) {
                try {
                    // Loading content on demand
                    URL url = MainController.class.getResource(TAB_MAP.get(newValue.getText()));
                    Parent root = (Parent) FXMLLoader.load(Objects.requireNonNull(url));
                    newValue.setContent(root);
                } catch (IOException e) {
                    log.error("", e);
                }
            } else {
                // Content is already loaded. Update it if necessary.
                Parent root = (Parent) newValue.getContent();
                // Optionally get the controller from Map and manipulate the content
                // via its controller.
            }
        });
        tabPane.getSelectionModel().selectFirst();
    }
}
