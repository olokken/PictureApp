package Team6;

import Team6.services.AppLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

/**
 * JavaFX App.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class App extends Application {

    private static Scene scene;

    /**
     * The main starting point of the application.
     *
     * @param args Command line arguments provided during startup.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Starts the application.
     * If app won't start, a {@link IOException} will be thrown.
     *
     * @param stage The stage.
     */
    @Override
    public void start(Stage stage) throws IOException {
        try {
            scene = new Scene(Objects.requireNonNull(loadFXML("LoginView")));
            stage.setScene(scene);
            Image image = new Image(new FileInputStream("./images/icon_small.png"));
            stage.getIcons().add(image);
            stage.show();
            scene.setFill(Color.TRANSPARENT);
        } catch (IOException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    /**
     * Sets the scene with the given fxml string.
     *
     * @param fxml The fxml string.
     */
    public static void setRoot(String fxml) throws IOException {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    /**
     * Loads fxml files from the given string.
     * If fxml can't load, a {@link IOException} will be thrown.
     *
     * @param fxml The fxml string.
     * @return Loaded fxml file.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
            return fxmlLoader.load();
        } catch (IOException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
        return null;
    }
}