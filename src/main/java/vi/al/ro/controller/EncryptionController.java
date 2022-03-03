package vi.al.ro.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import vi.al.ro.service.CryptographyService;
import vi.al.ro.service.KeyStoreService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class EncryptionController {

    @FXML
    private TextField tfFilePath;

    @FXML
    private TextField tfKeyPath;

    private final FileChooser fileChooser = new FileChooser();

    /**
     * Находит файл для шифрования
     * @param event
     */
    @FXML
    void onFileOpenClick(ActionEvent event) {
        final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        tfFilePath.setText(file.getPath());
    }

    /**
     * Находит файл ключа
     * @param event
     */
    @FXML
    void onKeyOpenClick(ActionEvent event) {
        final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        tfKeyPath.setText(file.getPath());
    }

    /**
     * Зашифровывает
     * @param event
     */
    @FXML
    void onEncryptClick(ActionEvent event) {
        File publicKeyFile = new File(tfKeyPath.getText());
        if (!publicKeyFile.exists() || publicKeyFile.isDirectory()) {
            System.err.println("Error!");
            return;
        }
        PublicKey publicKey = null;
        try {
            publicKey = KeyStoreService.readPublicKey(publicKeyFile);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return;
        }
        byte[] encryptedByteArray;
        try {
            encryptedByteArray = CryptographyService.encrypt(publicKey, "This is horosho!".getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("byte  array = [" + new String(encryptedByteArray) + "]");
    }
}
