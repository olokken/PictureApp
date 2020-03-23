package Team6;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import entities.Album;
import entities.PictureInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;


public class PrimaryController implements Initializable {
    ObservableList<Album> list;
    @FXML
    ListView<Album> albumList;


    public void fyllListe() {
        ArrayList<Album> a = new ArrayList<>();
        Album b = new Album("Sommer");
        Album c = new Album("Vinter");
        Album d = new Album("Høst");
        Album e = new Album("Vår");
        Collections.addAll(a,b,c,d,e);
        list = FXCollections.observableArrayList(a);
        albumList.getItems().addAll(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fyllListe();
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
