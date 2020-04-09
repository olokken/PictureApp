package Team6;

import entities.Album;
import entities.Picture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    Album album = new Album(Context.getInstance().currentAlbum().getName());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        album.setPictures(Context.getInstance().currentAlbum().getPictures());
        album.setId(Context.getInstance().currentAlbum().getId());
        pictureSetup();
        metadataSetup();
        listSetup();
    }


    void pictureSetup() {
        Image image = null;
        try {
            image = new Image(new FileInputStream(album.getPictures().get(index).getFilepath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setImage(image);
    }

    @FXML
    void deletePicture() throws IOException {
        Picture picture = album.getPictures().get(index);
        pictureService.deletePicture(picture.getId(), album.getId());
        Context.getInstance().currentAlbum().removePicture(picture);
        App.setRoot("secondary");
    }

    void listSetup() {
        ObservableList<String> list = FXCollections.observableArrayList(album.getPictures().get(index).getTags());
        listView.setItems(list);
    }


    @FXML
    private void switchToSecondary() throws IOException {
        if(album.getId() == -1) {
            App.setRoot("search");
        }
        else {
            App.setRoot("secondary");
        }
    }

    void metadataSetup() {
        Picture picture = album.getPictures().get(index);
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
