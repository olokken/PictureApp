package Team6;

import entities.Album;
import entities.Picture;
import idk.AppLogger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import services.PictureService;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

public class TertiaryController extends BaseController implements Initializable {
    @FXML
    HBox hBox;
    @FXML
    Button deleteButton;
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
    BorderPane borderPane;
    @FXML
    ImageView imageView;
    @FXML
    ListView<String> listView;

    PictureService pictureService = new PictureService();
    int index = Context.getInstance().currentIndex();
    Album album = new Album();

    public TertiaryController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        albumSetup();
        pictureSetup();
        metadataSetup();
        listSetup();
        deleteButtonSetup();
    }

    void albumSetup() {
        album.setName(Context.getInstance().currentAlbum().getName());
        album.setPictures(Context.getInstance().currentAlbum().getPictures());
        album.setId(Context.getInstance().currentAlbum().getId());
    }

    void deleteButtonSetup() {
        if (album.getId() <= 0){
            hBox.getChildren().remove(deleteButton);
        }
    }


    void pictureSetup() {
        Image image = null;
        try {
            image = new Image(new FileInputStream(album.getPictures().get(index).getFilepath()));
        } catch (FileNotFoundException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
        imageView.setImage(image);
    }

    @FXML
    void deletePicture() throws IOException {
        try{
            Picture picture = album.getPictures().get(index);
            pictureService.deletePicture(picture.getId(), album.getId());
            Context.getInstance().currentAlbum().removePicture(picture);
            App.setRoot("secondary");
        } catch (IOException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }

    }

    void listSetup() {
        ObservableList<String> list = FXCollections.observableArrayList(album.getPictures().get(index).getTags());
        listView.setItems(list);
    }


    @FXML
    private void switchToSecondary() throws IOException {
        try{
            String lastScene = Context.getInstance().getLastScene();
            App.setRoot(lastScene);
        } catch (IOException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }

    }

    void metadataSetup() {
        Picture picture = album.getPictures().get(index);
        if (picture.getFileName()!= null) {
            fileName.setText("File name : " +picture.getFileName());
        }
        fileSize.setText("File size : " + Double.toString(picture.getFileSize()));
        iso.setText("ISO : " + Integer.toString(picture.getISO()));
        shutterspeed.setText("Shutterspeed : " + Integer.toString(picture.getShutterSpeed()));
        exposureTime.setText("Exposure time : " + Double.toString(picture.getExposureTime()));
        latitude.setText("Latitude : " + Double.toString(picture.getLatitude()));
        longitude.setText("Longitude : " + Double.toString(picture.getLongitude()));
    }

    public void addTag(ActionEvent actionEvent) {
        Optional<String> result = showInputDialog("Add new tag", "Enter tag :");
        if (result.isPresent()) {
            pictureService.addTag(album.getPictures().get(index), result.get());
            album.getPictures().get(index).getTags().add(result.get());
            listSetup();
        }
    }


    public void deleteTag(ActionEvent actionEvent) {
        if (listView.getSelectionModel().getSelectedIndex() > -1) {
            Picture picture = album.getPictures().get(index);
            String tag = listView.getSelectionModel().getSelectedItem();
            pictureService.deleteTag(picture, tag);
            picture.getTags().remove(tag);
            listSetup();
        }
    }
    public void mordi(){

    }
}
