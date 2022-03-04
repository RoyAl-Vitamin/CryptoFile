package vi.al.ro.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vi.al.ro.service.CryptographyService;
import vi.al.ro.service.KeyStorePkcs12Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static vi.al.ro.constants.KeyStoreData.ALIAS;
import static vi.al.ro.constants.KeyStoreData.PASSWORD;

public class EncryptionController {

    private static final Logger log = LogManager.getLogger(EncryptionController.class);

    @FXML
    private TextField tfFilePath;

    @FXML
    private TextField tfKeyPath;

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    private final FileChooser fileChooser = new FileChooser();

    /**
     * Находит файл для шифрования
     * @param event
     */
    @FXML
    void onFileOpenClick(ActionEvent event) {
        final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            tfFilePath.setText(file.getPath());
            log.info("File path {}", file.getPath());
        }
    }

    /**
     * Находит файл ключа
     * @param event
     */
    @FXML
    void onKeyOpenClick(ActionEvent event) {
        final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            tfKeyPath.setText(file.getPath());
            log.info("File path {}", file.getPath());
        }
    }

    /**
     * Зашифровывает
     * @param event
     */
    @FXML
    void onEncryptClick(ActionEvent event) {
        File keyStoreFile = new File(tfKeyPath.getText());
        if (!keyStoreFile.exists() || keyStoreFile.isDirectory()) {
            log.error("Полученный файл хранилища ключей не существует или является директорией");
            return;
        }
        File inFile = new File(tfFilePath.getText());
        if (!inFile.exists() || inFile.isDirectory()) {
            log.error("Полученный файл для зашифровки не существует или является директорией");
            return;
        }
        KeyStorePkcs12Service service;
        try {
            service = new KeyStorePkcs12Service(ALIAS, PASSWORD, keyStoreFile);
        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException e) {
            log.error("", e);
            return;
        }
//        KeyStoreGeneratorService service;
//        try {
//            service = new KeyStoreGeneratorService();
//        } catch (NoSuchAlgorithmException e) {
//            log.error("", e);
//            return;
//        }
        File outFile = new File(TEMP_DIR, String.format("%s.encrypted", inFile.getName()));
        try {
            if (!outFile.exists() && !outFile.createNewFile()) {
                throw new IOException();
            }
        } catch (IOException e) {
            log.error("", e);
            return;
        }
        try {
            CryptographyService.encryptFile(inFile, outFile, service.getPublicKey());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            log.error("", e);
            return;
        }
        log.info("Encrypted file store in {} file size = {}", outFile.getAbsolutePath(), outFile.length());
    }
}
