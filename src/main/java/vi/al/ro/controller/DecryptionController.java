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
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

public class DecryptionController {

    @FXML
    private TextField tfFilePath;

    @FXML
    private TextField tfKeyPath;

    private final FileChooser fileChooser = new FileChooser();

    /**
     * Находит файл для расшифровки
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
     * Расшифровывает
     * @param event
     */
    @FXML
    void onDecryptClick(ActionEvent event) {

        File privateKeyFile = new File(tfKeyPath.getText());
        if (!privateKeyFile.exists() || privateKeyFile.isDirectory()) {
            System.err.println("Error!");
            return;
        }
        PrivateKey privateKey = null;
        try {
            privateKey = KeyStoreService.readPrivateKey(privateKeyFile);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return;
        }

        byte[] decryptedByteArray;
        try {
            decryptedByteArray = CryptographyService.decrypt(privateKey, "This is horosho!".getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("byte  array = [" + new String(decryptedByteArray) + "]");
    }
}
