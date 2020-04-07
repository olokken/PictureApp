package Team6;

import entities.Picture;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import services.PictureService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class TertiaryController implements Initializable {
    @FXML
    Text fileSize;
    @FXML
    Text date;
    @FXML
    Text iso;
    @FXML
    Text shutterspeed;
    @FXML
    Text exposureTime;
    @FXML
    Text flash;
    @FXML
    Text latitude;
    @FXML
    Text longitude;
    @FXML
    Text fileName;
    @FXML
    Text filePath;
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
        metadataSetup();
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

    @FXML
    void deletePicture() {
        pictureService.deletePicture(pic.get(index));
    }


    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    void metadataSetup() {
        Picture picture = pic.get(index);
        if (filePath != null) {
            filePath.setText(picture.getFilepath());
        }
        if (picture.getFileName()!= null) {
            fileName.setText(picture.getFileName());
        }
        fileSize.setText(Double.toString(picture.getFileSize()));
        iso.setText(Integer.toString(picture.getISO()));
        shutterspeed.setText(Integer.toString(picture.getShutterSpeed()));
        exposureTime.setText(Double.toString(picture.getExposureTime()));
        latitude.setText(Double.toString(picture.getLatitude()));
        longitude.setText(Double.toString(picture.getLongitude()));
    }
}
