package Team6;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import entities.Album;
import entities.Picture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import services.PictureService;



public class SecondaryController implements Initializable {
    PictureService pictureService = new PictureService();
    Album album = new Album();
    @FXML
    Text albumName;
    @FXML
    AnchorPane anchorPane;
    @FXML
    TilePane tilePane;
    private int nRows = 3;
    private int nCols = 3;
    private static final double ELEMENT_SIZE = 100;
    private static final double GAP = ELEMENT_SIZE/10;




    public void fillList () {
        album.setPictures(pictureService.getAllPictures(album.getId()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        album.setName(Context.getInstance().currentAlbum().getName());
        album.setId(Context.getInstance().currentAlbum().getId());
        albumName.setText(album.getName());
        tilePane.setPrefColumns(nCols);
        tilePane.setPrefRows(nRows);
        tilePane.setHgap(GAP);
        tilePane.setVgap(GAP);
        fillList();
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    private void createElements() throws FileNotFoundException {
        tilePane.getChildren().clear();
        int count = 0;
        for (int i = 0; i < nCols; i++) {
            for (int j = 0; j < nRows; j++) {
                if(count<album.getPictures().size()) {
                    tilePane.getChildren().add(createPage(count));
                    count++;
                }
            }
        }
    }

    public VBox createPage(int index) throws FileNotFoundException {
        VBox pageBox = new VBox();
        ImageView view = new ImageView();
        view.setImage(viewAblePictures().get(index));
        view.setFitHeight(ELEMENT_SIZE);
        view.setFitWidth(ELEMENT_SIZE);
        view.setSmooth(true);
        view.setCache(true);
        pageBox.getChildren().add(view);
        return  pageBox;
    }

    public ArrayList<Image> viewAblePictures() throws FileNotFoundException {
        ArrayList<Image> images = new ArrayList<>();
        for (Picture p : album.getPictures()) {
            Image i = new Image(new FileInputStream(p.getFilepath()));
            images.add(i);
        }
        return images;
    }
    
    public void sortIso(ActionEvent actionEvent) throws FileNotFoundException {
        album.sortIso();
        createElements();
    }

    public void sortFlash(ActionEvent actionEvent) throws FileNotFoundException {
        album.sortFlashUsed();
        createElements();
    }

    public void sortShutterSpeed(ActionEvent actionEvent) throws FileNotFoundException {
        album.sortShutterSpeed();
        createElements();
    }

    public void sortFileSize(ActionEvent actionEvent) throws FileNotFoundException {
        album.sortFileSize();
        createElements();
    }

    public void sortExposureTime(ActionEvent actionEvent) throws FileNotFoundException {
        album.sortExposureTime();
        createElements();
    }

    public void sortDate() throws FileNotFoundException {
        album.sortDate();
        createElements();
    }

    public void deletePicture(ActionEvent actionEvent) {
    }

    public void addPicture(ActionEvent actionEvent) {
        final DirectoryChooser dir = new DirectoryChooser();
        File file = dir.showDialog(anchorPane.getScene().getWindow());
        file.getPath();
    }
}