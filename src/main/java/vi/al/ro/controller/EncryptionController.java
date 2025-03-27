package vi.al.ro.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import vi.al.ro.service.cryptography.CryptographyService;
import vi.al.ro.service.cryptography.DesEcbPkcs5PaddingCryptographyService;

import java.io.File;
import java.io.IOException;

@Log4j2
public class EncryptionController {

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
//        File keyStoreFile = new File(tfKeyPath.getText());
//        if (!keyStoreFile.exists() || keyStoreFile.isDirectory()) {
//            log.error("Полученный файл хранилища ключей не существует или является директорией");
//            return;
//        }
        File inFile = new File(tfFilePath.getText());
        if (!inFile.exists() || inFile.isDirectory()) {
            log.error("Полученный файл для зашифровки не существует или является директорией");
            return;
        }
//        KeyStoreService service;
//        try {
//            service = new Pkcs12KeyStoreService(ALIAS, PASSWORD, keyStoreFile);
//        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException e) {
//            log.error("", e);
//            return;
//        }
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
//        CryptographyService cryptographyService = new JCECryptographyService(service);
        CryptographyService cryptographyService = new DesEcbPkcs5PaddingCryptographyService();
        try {
            cryptographyService.encryptFile(inFile, outFile);
        } catch (IOException e) {
            log.error("", e);
            return;
        }
        log.info("Encrypted file store in {} file size = {}", outFile.getAbsolutePath(), outFile.length());
    }
}
