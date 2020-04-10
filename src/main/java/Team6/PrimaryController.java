package Team6;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;

import entities.Album;
import entities.Picture;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.AlbumService;
import services.PicLdLogger;
import services.PictureService;


public class PrimaryController implements Initializable  {
    @FXML
    TextField textField;
    @FXML
    VBox vBox;
    @FXML
    AnchorPane anchorPane;
    @FXML
    ListView<Album> albumView;

    ArrayList<Album> yourAlbums = new ArrayList<Album>();
    ObservableList<Album> list = FXCollections.observableArrayList(yourAlbums);
    AlbumService albumService = new AlbumService();
    PictureService pictureService = new PictureService();
    User user = Context.getInstance().currentUser();

    //Create logger object from PicLdLogger class.
    private PicLdLogger picLdLogger = new PicLdLogger();

    public PrimaryController() throws IOException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillListView();
        doubleClick();
        search();
    }

    public void fillListView() {
        yourAlbums = albumService.getAllAlbums(user.getId());
        Album album = new Album("All Photos");
        album.setId(0);
        yourAlbums.add(0, album);
        albumView.getItems().addAll(yourAlbums);
    }


    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }


    public void createAlbum(ActionEvent actionEvent) throws IOException {
        TextInputDialog t = new TextInputDialog();
        t.setTitle("Album");
        t.setHeaderText("Create new album");
        t.setContentText("Enter name: ");
        Optional<String> result = t.showAndWait();
        if (result.isPresent()) {
            albumService.createAlbum(result.get(), user.getId());
            yourAlbums.add(new Album(result.get(), user.getId()));
            list = FXCollections.observableArrayList(yourAlbums);
            albumView.setItems(list);
        }
    }

    public void deleteAlbum(ActionEvent actionEvent) {
        if (albumView.getSelectionModel().getSelectedIndex() > -1) {
            Album album = albumView.getSelectionModel().getSelectedItem();
            albumService.deleteAlbum(album);
            yourAlbums.remove(album);
            list = FXCollections.observableArrayList(yourAlbums);
            albumView.setItems(list);
        }
    }

    public void doubleClick() {
        albumView.setOnMouseClicked(e -> {
            if(e.getClickCount() == 2 && albumView.getSelectionModel().getSelectedIndex() > -1) {
                Context.getInstance().currentAlbum().setId(albumView.getSelectionModel().getSelectedItem().getId());
                Context.getInstance().currentAlbum().setName(albumView.getSelectionModel().getSelectedItem().getName());
                try {
                    App.setRoot("secondary");
                } catch (IOException ex) {
                    picLdLogger.getLogger().log(Level.FINE, ex.getMessage());
                }
            }
        });
    }

    public void openAlbum() throws IOException {
        if (albumView.getSelectionModel().getSelectedIndex() > -1) {
            Context.getInstance().currentAlbum().setId(albumView.getSelectionModel().getSelectedItem().getId());
            Context.getInstance().currentAlbum().setName(albumView.getSelectionModel().getSelectedItem().getName());
            App.setRoot("secondary");
        }
    }

    public void search() {
        textField.setOnMouseClicked(e -> {
            try {
                App.setRoot("search");
            } catch (IOException ex) {
                picLdLogger.getLogger().log(Level.FINE, ex.getMessage());
            }
        });
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        App.setRoot("login");
    }
}
