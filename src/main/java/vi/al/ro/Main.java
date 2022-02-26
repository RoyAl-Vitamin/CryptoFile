package vi.al.ro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxmlEncrypt.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 480, 240);
        MainController controller = (MainController) fxmlLoader.getController();
        controller.setStage(stage);
        stage.setTitle("Encryption!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}