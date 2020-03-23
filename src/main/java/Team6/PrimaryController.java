package Team6;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.Album;
import entities.PictureInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PrimaryController implements Initializable  {
    ArrayList<Album> yourAlbums = new ArrayList<Album>();
    ObservableList<Album> list = FXCollections.observableArrayList(yourAlbums);
    @FXML
    ListView<Album> albumList;


    public void fillList() {
        Album b = new Album("Sommer");
        Album c = new Album("Vinter");
        Album d = new Album("Høst");
        Album e = new Album("Vår");
        yourAlbums.add(b);
        yourAlbums.add(c);
        yourAlbums.add(d);
        yourAlbums.add(e);
        albumList.getItems().addAll(yourAlbums);
    }


    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillList();
    }


    public void createAlbum(ActionEvent actionEvent) throws IOException {
        TextInputDialog t = new TextInputDialog("Create new album");
        Optional<String> result = t.showAndWait();
        if (result.isPresent()) {
            yourAlbums.add(new Album(result.get()));
            list = FXCollections.observableArrayList(yourAlbums);
            albumList.refresh();
        }
    }
}
