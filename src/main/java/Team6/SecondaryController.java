package Team6;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import entities.Album;
import entities.Picture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import services.PictureService;



public class SecondaryController implements Initializable {
    PictureService pictureService = new PictureService();
    ListView<Picture> pictureView;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }


    public void fillList () {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillList();
    }


}