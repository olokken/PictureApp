package Team6;

import entities.Album;
import entities.Picture;
import idk.AppLogger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import services.PictureService;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

/**
 * Controller for PictureView.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class TertiaryController extends BaseController implements Initializable {
    @FXML
    HBox hBox;
    @FXML
    Button deleteButton;
    @FXML
    TextArea fileSize;
    @FXML
    TextArea date;
    @FXML
    TextArea iso;
    @FXML
    TextArea shutterspeed;
    @FXML
    TextArea exposureTime;
    @FXML
    TextArea flash;
    @FXML
    TextArea latitude;
    @FXML
    TextArea longitude;
    @FXML
    TextArea fileName;
    @FXML
    BorderPane borderPane;
    @FXML
    ImageView imageView;
    @FXML
    ListView<String> listView;

    PictureService pictureService = new PictureService();
    int index = Context.getInstance().currentIndex();
    Album album = new Album();

    /**
     * Constructor that creates an instance of PictureView, initialising the instance.
     */
    public TertiaryController(){
    }

    /**
     * Initialize the PictureView.
     *
     * @param url The url.
     * @param resourceBundle The resource bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        albumSetup();
        pictureSetup();
        metadataSetup();
        listSetup();
        deleteButtonSetup();
    }

    /**
     * Sets up album.
     */
    void albumSetup() {
        album.setName(Context.getInstance().currentAlbum().getName());
        album.setPictures(Context.getInstance().currentAlbum().getPictures());
        album.setId(Context.getInstance().currentAlbum().getId());
    }

    /**
     * Sets up delete button.
     */
    void deleteButtonSetup() {
        if (album.getId() <= 0){
            hBox.getChildren().remove(deleteButton);
        }
    }


    /**
     * Sets up picture.
     */
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

    /**
     * Deletes picture shown.
     */
    @FXML
    void deletePicture() {
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

    /**
     * Sets up list view with tags.
     */
    void listSetup() {
        ObservableList<String> list = FXCollections.observableArrayList(album.getPictures().get(index).getTags());
        listView.setItems(list);
    }


    @FXML
    private void switchToSecondary() throws IOException {
        try{
            switchScene("tertiary", "secondary");
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
        iso.setText("ISO : " + Integer.toString(picture.getIso()));
        shutterspeed.setText("Shutterspeed : " + Integer.toString(picture.getShutterSpeed()));
        exposureTime.setText("Exposure time : " + Double.toString(picture.getExposureTime()));
        latitude.setText("Latitude : " + Double.toString(picture.getLatitude()));
        longitude.setText("Longitude : " + Double.toString(picture.getLongitude()));
    }

    /**
     * Add tag to picture.
     */
    public void addTag() {
        Optional<String> result = showInputDialog("Add new tag", "Enter tag :");
        if (result.isPresent()) {
            pictureService.addTag(album.getPictures().get(index), result.get());
            album.getPictures().get(index).getTags().add(result.get());
            listSetup();
        }
    }

    /**
     * Deletes selected tag from picture.
     */
    public void deleteTag() {
        if (listView.getSelectionModel().getSelectedIndex() > -1) {
            Picture picture = album.getPictures().get(index);
            String tag = listView.getSelectionModel().getSelectedItem();
            pictureService.deleteTag(picture, tag);
            picture.getTags().remove(tag);
            listSetup();
        }
    }
}
