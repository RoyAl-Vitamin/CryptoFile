package vi.al.ro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import vi.al.ro.controller.MainController;
import vi.al.ro.service.db.DataBaseService;
import vi.al.ro.service.scheduled.CryptographyExecutorService;

import java.io.IOException;
import java.security.Security;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Security.addProvider(new BouncyCastleProvider());

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxmlMain.fxml"));
        fxmlLoader.setController(new MainController());
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CryptoFile");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        DataBaseService.close();
        CryptographyExecutorService.shutdown();
        super.stop();
    }

    public static void main(String[] args) {
        DataBaseService.getInstance();
        CryptographyExecutorService.getInstance();
        launch();
    }
}