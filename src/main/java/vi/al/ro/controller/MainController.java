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
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final Logger log = LogManager.getLogger(MainController.class);

    private static final URL XML_ENCRYPT_URL = MainController.class.getResource("fxmlEncrypt.fxml");

    private static final URL XML_DECRYPT_URL = MainController.class.getResource("fxmlDecrypt.fxml");

    private static final URL XML_KEY_STORE_URL = MainController.class.getResource("fxmlKeyStore.fxml");

    @FXML
    private Tab tabDecrypt;

    @FXML
    private Tab tabEncrypt;

    @FXML
    private Tab tabKeyStore;

    @FXML
    private TabPane tabPane;

    @Override
    public void initialize(URL url1, ResourceBundle resourceBundle) {
        tabEncrypt.setContent(getEncryptParent());
        tabDecrypt.setContent(getDecryptParent());
        tabKeyStore.setContent(getKeyStoreParent());
    }

    private Parent getEncryptParent() {
        return getParent(XML_ENCRYPT_URL, new EncryptionController());
    }

    private Parent getDecryptParent() {
        return getParent(XML_DECRYPT_URL, new DecryptionController());
    }

    private Parent getKeyStoreParent() {
        return getParent(XML_KEY_STORE_URL, new KeyStoreController());
    }

    private Parent getParent(URL fxmlUrl, Object controller) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlUrl);
        loader.setController(controller);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        return root;
    }
}
