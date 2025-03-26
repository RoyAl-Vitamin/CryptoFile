package vi.al.ro.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import vi.al.ro.service.message_digest.Md5MessageDigestService;
import vi.al.ro.service.message_digest.MessageDigestService;

import java.io.File;
import java.util.Objects;

@Log4j2
public class MessageDigestController {

    @FXML
    private TextField tfFilePath;

    @FXML
    private TextField tfMd;

    private final FileChooser fileChooser = new FileChooser();

    private final MessageDigestService messageDigestService = new Md5MessageDigestService();

    @FXML
    void onFileOpenClick(ActionEvent event) {
        final Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (Objects.nonNull(file)) {
            tfFilePath.setText(file.getPath());
            log.info("File path {}", file.getPath());
        }
    }

    @FXML
    void onMessageDigestCalcClick(ActionEvent event) {
        File file = new File(tfFilePath.getText());
        if (!file.exists() || file.isDirectory()) {
            return;
        }
        String md = messageDigestService.getMessageDigest(file);
        if (Objects.isNull(md)) {
            return;
        }
        tfMd.setText(md);
    }
}
