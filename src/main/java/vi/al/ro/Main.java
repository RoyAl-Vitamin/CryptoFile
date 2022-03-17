package vi.al.ro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vi.al.ro.service.DataBaseService;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxmlMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CryptoFile");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DataBaseService.init();
        launch();
    }
}