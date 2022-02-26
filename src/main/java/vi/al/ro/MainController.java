package vi.al.ro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

public class MainController {

    private final FileChooser fileChooser = new FileChooser();

    private Stage stage;

    @FXML
    private TextField tfFilePath;

    @FXML
    private TextField tfKeyPath;

    /**
     * Находит файл для шифрования
     * @param event
     */
    @FXML
    void onFileOpenClick(ActionEvent event) {
        File file = fileChooser.showOpenDialog(stage);
        tfFilePath.setText(file.getPath());
    }

    /**
     * Находит файл ключа
     * @param event
     */
    @FXML
    void onKeyOpenClick(ActionEvent event) {
        File file = fileChooser.showOpenDialog(stage);
        tfKeyPath.setText(file.getPath());
    }

    /**
     * Зашифровывает
     * @param event
     */
    @FXML
    void onEncryptClick(ActionEvent event) {

    }

    protected void setStage(Stage stage) {
        this.stage = stage;
    }
}
