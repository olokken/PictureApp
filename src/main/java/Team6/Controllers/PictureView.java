package Team6.Controllers;

import Team6.App;
import Team6.entities.Album;
import Team6.entities.Picture;
import Team6.services.AppLogger;
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
import Team6.services.PictureService;
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
public class PictureView extends Base implements Initializable {
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
    public PictureView() throws IOException {
    }

    /**
     * Initialize the PictureView.
     *
     * @param url The url.
     * @param resourceBundle The resource bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupAlbum();
        setupPicture();
        setupMetadata();
        setupListView();
        setupDeleteButton();
    }

    /**
     * Sets up album.
     */
    void setupAlbum() {
        album.setName(Context.getInstance().currentAlbum().getName());
        album.setPictures(Context.getInstance().currentAlbum().getPictures());
        album.setId(Context.getInstance().currentAlbum().getId());
    }

    /**
     * Sets up delete button.
     */
    void setupDeleteButton() {
        if (album.getId() <= 0){
            hBox.getChildren().remove(deleteButton);
        }
    }

    /**
     * Sets up picture.
     */
    void setupPicture() {
        Image image = null;
        try {
            image = new Image(new FileInputStream(album.getPictures().get(index).getFilepath()));
        } catch (FileNotFoundException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setImage(image);
    }

    /**
     * Deletes picture shown.
     */
    @FXML
    void deletePicture() throws IOException {
        try{
            Picture picture = album.getPictures().get(index);
            pictureService.deletePicture(picture.getId(), album.getId());
            Context.getInstance().currentAlbum().removePicture(picture);
            switchScene("SearchView", "AlbumView");
        } catch (IOException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }

    }

    /**
     * Sets up list view with tags.
     */
    void setupListView() {
        ObservableList<String> list = FXCollections.observableArrayList(album.getPictures().get(index).getTags());
        listView.setItems(list);
    }

    /**
     * Switches to the scene used before PictureView.
     */
    @FXML
    private void switchView() throws IOException {
        try{
            switchScene("PictureView", Context.getInstance().getLastScene());
        } catch (IOException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }

    }

    /**
     * Sets up metadata information.
     */
    void setupMetadata() {
        Picture picture = album.getPictures().get(index);
        if (picture.getFileName()!= null) {
            fileName.setText("File name : " +picture.getFileName());
        }
        fileSize.setText("File size: " + Double.toString(picture.getFileSize()) + "bytes");
        iso.setText("ISO: " + Integer.toString(picture.getIso()));
        shutterspeed.setText("Shutter speed: " + Integer.toString(picture.getShutterSpeed()) + " s");
        exposureTime.setText("Exposure time: " + Double.toString(picture.getExposureTime()) + " s");
        latitude.setText("Latitude: " + Double.toString(picture.getLatitude()) + " degrees");
        longitude.setText("Longitude: " + Double.toString(picture.getLongitude()) + " degrees");
    }

    /**
     * Add tag to picture.
     */
    public void addTag() {
        Optional<String> result = showInputDialog("Add new tag", "Enter tag :");
        if (result.isPresent()) {
            pictureService.addTag(album.getPictures().get(index), result.get());
            album.getPictures().get(index).getTags().add(result.get());
            setupListView();
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
            setupListView();
        } else {
            showInformationDialog("Tag not chosen", "You have to choose the tag you want to delete from the list");
        }
    }
}
