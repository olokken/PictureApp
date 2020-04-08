package Team6;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.Album;
import entities.Picture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.AlbumService;
import services.PictureService;


public class PrimaryController implements Initializable  {
    @FXML
    VBox vBox;
    @FXML
    AnchorPane anchorPane;
    ArrayList<Album> yourAlbums = new ArrayList<Album>();
    ObservableList<Album> list = FXCollections.observableArrayList(yourAlbums);
    AlbumService albumService = new AlbumService();
    @FXML
    ListView<Album> albumView;


    public void fillList() {
        yourAlbums = albumService.getAllAlbums();
        Album album = new Album("All Photos");
        album.setId(0);
        yourAlbums.add(0, album);
        albumView.getItems().addAll(yourAlbums);
    }

    void bind() {
        vBox.prefHeightProperty().bind(anchorPane.widthProperty());
        vBox.prefHeightProperty().bind(anchorPane.heightProperty());
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillList();
        doubleClick();
    }


    public void createAlbum(ActionEvent actionEvent) throws IOException {
        TextInputDialog t = new TextInputDialog();
        t.setTitle("Album");
        t.setHeaderText("Create new album");
        t.setContentText("Enter name: ");
        Optional<String> result = t.showAndWait();
        if (result.isPresent()) {
            albumService.createAlbum(result.get());
            fillList();
            list = FXCollections.observableArrayList(yourAlbums);
            albumView.setItems(list);
        }
    }

    public void deleteAlbum(ActionEvent actionEvent) {
        if (albumView.getSelectionModel().getSelectedIndex() > -1) {
            albumService.deleteAlbum(albumView.getSelectionModel().getSelectedItem());
            fillList();
            list = FXCollections.observableArrayList(yourAlbums);
            albumView.setItems(list);
        }
        else {
            System.out.println("velg en gokar først");
        }
    }

    public void doubleClick() {
        albumView.setOnMouseClicked(e -> {
            if(e.getClickCount() == 2 || albumView.getSelectionModel().getSelectedIndex() > -1) {
                Context.getInstance().currentAlbum().setId(albumView.getSelectionModel().getSelectedItem().getId());
                Context.getInstance().currentAlbum().setName(albumView.getSelectionModel().getSelectedItem().getName());
                try {
                    App.setRoot("secondary");
                } catch (IOException ex) {
                    ex.printStackTrace();
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

}
