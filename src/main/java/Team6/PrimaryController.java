package Team6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import entities.Album;
import entities.PictureInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;


public class PrimaryController {
    ObservableList<Album> list;
    ListView<Album> albumList;

    public void fyllListe() {
        ArrayList<Album> a = new ArrayList<Album>();
        Album b = new Album("Sommer");
        Album c = new Album("Sommer");
        Album d = new Album("Sommer");
        Album e = new Album("Sommer");
        Collections.addAll(a,b,c,d,e);
        list = FXCollections.observableArrayList(a);
        albumList.getItems().addAll(list);
    }

}
