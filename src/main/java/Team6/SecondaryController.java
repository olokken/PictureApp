package Team6;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import entities.Album;
import entities.Picture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import services.PictureService;



public class SecondaryController implements Initializable {
    @FXML
    ImageView mapViewIcon;
    @FXML
    Button addButton;
    @FXML
    Button deleteButton;
    @FXML
    ScrollPane scrollPane;
    @FXML
    SplitPane splitPane;
    @FXML
    Text albumName;
    @FXML
    AnchorPane anchorPane;

    PictureService pictureService = new PictureService();
    Album album = new Album();
    TilePane tilePane = new TilePane();

    private static double ELEMENT_SIZE = 170;
    private static final double GAP = ELEMENT_SIZE/10;

    //Create logger object from PicLdLogger class.
    //private PicLdLogger picLdLogger = new PicLdLogger();

    public SecondaryController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        albumSetup();
        fillList();
        pictureViewSetup();
        createElements();
        buttonSetup();
        try {
            mapViewSetup();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void albumSetup() {
        album.setName(Context.getInstance().currentAlbum().getName());
        album.setId(Context.getInstance().currentAlbum().getId());
        albumName.setText(album.getName());
    }

    void buttonSetup() {
        if (album.getId() < 0) {
            anchorPane.getChildren().removeAll(addButton, deleteButton);
        }
    }


    public void fillList () {
        if (Context.getInstance().currentAlbum().getPictures() == null) {
            ArrayList<Picture> pics = pictureService.getAllPictures(album.getId(), Context.getInstance().currentUser().getId());
            album.setPictures(pics);
        }
        else {
            album.setPictures(Context.getInstance().currentAlbum().getPictures());
        }
    }


    void pictureViewSetup() {
        tilePane.setHgap(GAP);
        tilePane.setVgap(GAP);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tilePane);
    }

    @FXML
    private void switchToPrimary() throws IOException {
        Context.getInstance().currentAlbum().setPictures(null);
        App.setRoot("primary");
    }


    void createElements() {
        tilePane.getChildren().clear();
        tilePane.getChildren().addAll(createPages());
    }

     List<ImageView> createImageViews() {
        return album.getPictures().stream().map(x -> {
            Image image = null;
            try {
                image = new Image(new FileInputStream(x.getFilepath()));
            } catch (FileNotFoundException e) {
                //picLdLogger.getLogger().log(Level.FINE, e.getMessage());
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(ELEMENT_SIZE);
            imageView.setFitWidth(ELEMENT_SIZE);
            imageView.setSmooth(true);
            imageView.setCache(true);
            imageView.setOnMouseClicked(e -> {
                Context.getInstance().currentAlbum().setPictures(album.getPictures());
                Context.getInstance().currentAlbum().setId(album.getId());
                Context.getInstance().setIndex(album.getPictures().indexOf(x));
                try {
                    App.setRoot("tertiary");
                } catch (IOException ex) {
                    //picLdLogger.getLogger().log(Level.FINE, ex.getMessage());
                }
            });
            return imageView;
        }).collect(Collectors.toList());
    }

    List<VBox> createPages() {
        return createImageViews().stream().map(x -> {
            VBox vBox = new VBox();
            vBox.getChildren().add(x);
            return vBox;
        }).collect(Collectors.toList());
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

    public void addPicture(ActionEvent actionEvent) {
        final FileChooser dir = new FileChooser();
        List<String> filter = new ArrayList<>();
        Collections.addAll(filter, "*.jpg", "'.png", "*.jfif", "*.PNG");
        dir.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Pictures", filter));
        List<File> files = dir.showOpenMultipleDialog(anchorPane.getScene().getWindow());
        try {
            files.forEach(e -> {
                if (files.size() != 0) {
                    Picture p = null;
                    try {
                        p = new Picture(e.getPath());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    album.getPictures().add(p);
                    pictureService.createPicture(p, album.getId());
                    createElements();
                    ArrayList<Picture> pics = pictureService.getAllPictures(album.getId(), Context.getInstance().currentUser().getId());
                    album.setPictures(pics);
                }
            });
        } catch (NullPointerException e ) {
        }
    }

    public void reverseOrder(ActionEvent actionEvent) {
        album.reverseOrder();
        createElements();
    }

    void mapViewSetup() throws FileNotFoundException {
        Image image = new Image(new FileInputStream(".\\images\\globe.png"));
        mapViewIcon.setImage(image);
        mapViewIcon.setOnMouseClicked(e -> {
            Context.getInstance().currentAlbum().setPictures(album.getPictures());
            try {
                App.setRoot("map");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

    }


}