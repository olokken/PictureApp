package Team6;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import entities.Album;
import entities.Picture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.PictureService;



public class SecondaryController implements Initializable {
    PictureService pictureService = new PictureService();
    Album album = new Album();
    List<VBox> pics;

    @FXML
    SplitPane splitPane;
    @FXML
    Text albumName;
    @FXML
    AnchorPane anchorPane;
    @FXML
    TilePane tilePane;

    private static double ELEMENT_SIZE = 170;
    private static final double GAP = ELEMENT_SIZE/10;




    public void fillList () {
        album.setPictures(pictureService.getAllPictures(album.getId()));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        album.setName(Context.getInstance().currentAlbum().getName());
        album.setId(Context.getInstance().currentAlbum().getId());
        fillList();
        int nRows = album.getPictures().size()/3;
        int nCols = 3;
        albumName.setText(album.getName());
        tilePane.setPrefColumns(nCols);
        tilePane.setPrefRows(nRows);
        tilePane.setHgap(GAP);
        tilePane.setVgap(GAP);
        createElements();
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }


    private void createElements() {
        tilePane.getChildren().clear();
        pics = album.getPictures().stream().map(x -> {
            Image image = null;
            try {
                image = new Image(new FileInputStream(x.getFilepath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(ELEMENT_SIZE);
            imageView.setFitWidth(ELEMENT_SIZE);
            imageView.setSmooth(true);
            imageView.setCache(true);
            VBox pageBox = new VBox();
            pageBox.getChildren().add(imageView);
            return pageBox;
        }).collect(Collectors.toList());
        tilePane.getChildren().addAll(pics);
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

    public void deletePicture(ImageView v) {
        v.getImage().getUrl();
    }

    public void addPicture(ActionEvent actionEvent)  {
        final FileChooser dir = new FileChooser();
        File file = dir.showOpenDialog(anchorPane.getScene().getWindow());
        if (file.exists()) {
            Picture p = new Picture(file.getPath());
            album.getPictures().add(p);
            pictureService.createPicture(p, album.getId());
        }
    }
}