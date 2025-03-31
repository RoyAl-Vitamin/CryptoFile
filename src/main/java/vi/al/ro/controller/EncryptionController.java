package vi.al.ro.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import vi.al.ro.service.cryptography.DesEcbPkcs5PaddingCryptographyService;
import vi.al.ro.service.key.symmetric.DesKeyGeneratorService;
import vi.al.ro.service.key.symmetric.SymmetricKeyDto;
import vi.al.ro.service.key.symmetric.SymmetricKeyFileService;
import vi.al.ro.service.key.symmetric.SymmetricKeyService;
import vi.al.ro.service.scheduled.CryptographyExecutorService;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Future;

@Log4j2
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
     * Генерирует симметричный ключ
     * @param event
     */
    @FXML
    void onKeyGenerateClick(ActionEvent event) {
        final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Symmetric key", "*.ser"));
        File file = fileChooser.showSaveDialog(stage);
        if (file.isDirectory()) {
            log.error("Ошибка выбора файла");
            return;
        }
        if (!file.getPath().endsWith(".key")) {
            file = new File(file.getPath() + ".ser");
        }
        try {
            if (!file.exists() && !file.createNewFile()) {
                log.error("Не удалось создать файл");
            }
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        SymmetricKeyFileService.writeFile(new DesKeyGeneratorService().getKey(), file);
        tfKeyPath.setText(file.getPath());
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
        File outFile = null;
        try {
            outFile = getNewFile(event, inFile.getName()).orElseThrow(IOException::new);
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        if (Objects.isNull(tfKeyPath.getText()) || tfKeyPath.getText().isBlank()) {
            log.error("Файл ключа не задан");
            return;
        }
        File keyFile = new File(tfKeyPath.getText());
        if (!keyFile.exists() || keyFile.isDirectory()) {
            log.error("Полученный файл ключа не существует или является директорией");
            return;
        }
        SymmetricKeyService symmetricKeyService = new SymmetricKeyDto(SymmetricKeyFileService.readKey(keyFile));
//        CryptographyService cryptographyService = new JCECryptographyService(service);
        Future<Void> task = CryptographyExecutorService.getInstance().addTask(inFile, outFile, new DesEcbPkcs5PaddingCryptographyService(symmetricKeyService)::encryptFile);
    }

    private Optional<File> getNewFile(ActionEvent event, String originalFileName) {
        String fileExtension = "";
        if (originalFileName.lastIndexOf(".") != -1 && originalFileName.lastIndexOf(".") != originalFileName.length() - 1) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        }
        final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Encrypted file", "*.encrypted"));
        File file = fileChooser.showSaveDialog(stage);
        if (file.isDirectory()) {
            log.error("Ошибка выбора файла");
            return Optional.empty();
        }
        String encryptedFileExtension = null;
        if (fileExtension.isBlank()) {
            encryptedFileExtension = ".encrypted";
        } else {
            encryptedFileExtension = String.format(".%s.encrypted", fileExtension);
        }
        if (!file.getPath().endsWith(encryptedFileExtension)) {
            file = new File(file.getPath() + encryptedFileExtension);
        }
        try {
            if (!file.exists() && !file.createNewFile()) {
                log.error("Не удалось создать файл");
            }
            return Optional.of(file);
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }
}
