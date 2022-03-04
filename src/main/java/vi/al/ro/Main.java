package vi.al.ro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxmlEncrypt.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxmlDecrypt.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 240);
        stage.setTitle("Encryption!");
//        stage.setTitle("Decryption!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}