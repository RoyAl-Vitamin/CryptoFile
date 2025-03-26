package vi.al.ro.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class MainController implements Initializable {

    @FXML
    private Tab tabDecrypt;

    @FXML
    private Tab tabEncrypt;

    @FXML
    private Tab tabKeyStore;

    @FXML
    private Tab tabMessageDigest;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabEncrypt.setContent(getEncryptParent());
        tabDecrypt.setContent(getDecryptParent());
        tabKeyStore.setContent(getKeyStoreParent());
        tabMessageDigest.setContent(getMessageDigestParent());
    }

    private Parent getEncryptParent() {
        return getParent(MainController.class.getResource("fxmlEncrypt.fxml"), new EncryptionController());
    }

    private Parent getDecryptParent() {
        return getParent(MainController.class.getResource("fxmlDecrypt.fxml"), new DecryptionController());
    }

    private Parent getKeyStoreParent() {
        return getParent(MainController.class.getResource("fxmlKeyStore.fxml"), new KeyStoreController());
    }

    private Parent getMessageDigestParent() {
        return getParent(MainController.class.getResource("fxmlMessageDigest.fxml"), new MessageDigestController());
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
