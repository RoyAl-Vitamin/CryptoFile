package vi.al.ro.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import vi.al.ro.service.cryptography.DesEcbPkcs5PaddingCryptographyTaskService;
import vi.al.ro.service.key.symmetric.SymmetricKeyDto;
import vi.al.ro.service.key.symmetric.SymmetricKeyFileService;
import vi.al.ro.service.key.symmetric.SymmetricKeyService;
import vi.al.ro.service.scheduled.CryptographyExecutorService;
import vi.al.ro.service.scheduled.DecryptionTask;
import vi.al.ro.service.scheduled.TaskType;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Future;

@Log4j2
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
        if (file != null) {
            tfFilePath.setText(file.getPath());
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
        }
    }

    /**
     * Расшифровывает
     * @param event
     */
    @FXML
    void onDecryptClick(ActionEvent event) {
        File symmetrykeyFile = new File(tfKeyPath.getText());
        if (!symmetrykeyFile.exists() || symmetrykeyFile.isDirectory()) {
            log.error("Полученный файл хранилища ключей не существует или является директорией");
            return;
        }
        File inFile = new File(tfFilePath.getText());
        if (!inFile.exists() || inFile.isDirectory()) {
            log.error("Полученный файл для зашифровки не существует или является директорией");
            return;
        }
        File outFile = null;
        try {
            outFile = getNewFile(event, inFile.getName().substring(0, inFile.getName().lastIndexOf('.'))).orElseThrow(IOException::new);
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        SymmetricKeyService symmetricKeyService = new SymmetricKeyDto(SymmetricKeyFileService.readKey(symmetrykeyFile));
//        CryptographyService cryptographyService = new JCECryptographyService(service);
//        Future<Void> fTask = CryptographyExecutorService.getInstance().addTask(inFile, outFile, new DesEcbPkcs5PaddingCryptographyService(symmetricKeyService)::decryptFile);
        Task<Void> task = new DecryptionTask(inFile, outFile, new DesEcbPkcs5PaddingCryptographyTaskService(symmetricKeyService), TaskType.DECRYPT);
        task.messageProperty().addListener((observableValue, oldValue, newValue) -> log.info("NEW VALUE = {}, OLD VALUE = {}", newValue, oldValue));
        Future<?> fTask = CryptographyExecutorService.getInstance().addTask(task);
    }

    private Optional<File> getNewFile(ActionEvent event, String originalFileName) {
        final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All file", "*.*"));
        File file = fileChooser.showSaveDialog(stage);
        if (file.isDirectory()) {
            log.error("Ошибка выбора файла");
            return Optional.empty();
        }
        if (originalFileName.lastIndexOf(".") != -1) {
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            if (!file.getPath().endsWith(fileExtension)) {
                file = new File(file.getPath() + fileExtension);
            }
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
