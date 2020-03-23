package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        //GridPane root = new GridPane();
        primaryStage.setTitle("PicLd");
        primaryStage.setScene(new Scene(root, 443, 582));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
