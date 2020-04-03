package Team6;

import entities.Picture;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import services.PictureService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TertiaryController implements Initializable {
    @FXML
    BorderPane borderPane;
    @FXML
    ImageView imageView;
    @FXML
    ListView listView;
    PictureService pictureService = new PictureService();

    int index = Context.getInstance().currentIndex();
    List<Picture> pic = Context.getInstance().currentPictures();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pictureSetup();
    }


    void pictureSetup() {
        Image image = null;
        try {
            image = new Image(new FileInputStream(pic.get(index).getFilepath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setImage(image);
    }

    void deletePicture() {
        pictureService.deletePicture(pic.get(index));
    }

    void listSetup() {

    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
