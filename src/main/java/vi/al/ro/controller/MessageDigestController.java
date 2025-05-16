package vi.al.ro.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import vi.al.ro.service.message_digest.Md5MessageDigestService;
import vi.al.ro.service.message_digest.MessageDigestService;
import vi.al.ro.service.message_digest.Sha512MessageDigestService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

@Log4j2
public class MessageDigestController implements Initializable {

    @FXML
    private TextField tfFilePath;

    @FXML
    private TextField tfMd;

    @FXML
    private ComboBox<String> cbMdType;

    private MessageDigestService currentMessageDigestService;

    private final FileChooser fileChooser = new FileChooser();

    private static final Map<String, MessageDigestService> MESSAGE_DIGEST_SERVICE_MAP = new HashMap<>(){{
        MessageDigestService mdMd5 = new Md5MessageDigestService();
        put(mdMd5.getName(), mdMd5);
        MessageDigestService mdSha512 = new Sha512MessageDigestService();
        put(mdSha512.getName(), mdSha512);
    }};

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
        byte[] byteArray = null;
        try {
            byteArray = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
        String md = currentMessageDigestService.getMessageDigest(byteArray);
        if (Objects.isNull(md)) {
            return;
        }
        tfMd.setText(md);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> types = FXCollections.observableArrayList(MESSAGE_DIGEST_SERVICE_MAP.keySet().stream().toList());
        cbMdType.setItems(types);
        cbMdType.setValue(types.getFirst());
        cbMdType.setOnAction(event -> currentMessageDigestService = MESSAGE_DIGEST_SERVICE_MAP.get(cbMdType.getValue()));
        currentMessageDigestService = MESSAGE_DIGEST_SERVICE_MAP.get(cbMdType.getValue());
    }
}
