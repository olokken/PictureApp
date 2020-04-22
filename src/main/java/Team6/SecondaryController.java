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
import idk.AppLogger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import services.AlbumService;
import services.PdfHandler;
import services.PictureService;



public class SecondaryController extends BaseController implements Initializable {
    @FXML
    VBox vBox;
    @FXML
    Button deleteButton;
    @FXML
    Button addButton;
    @FXML
    MenuButton changeStatusButton;
    @FXML
    ImageView pdfIcon;
    @FXML
    Button createAlbumButton;
    @FXML
    ImageView mapViewIcon;
    @FXML
    ScrollPane scrollPane;
    @FXML
    BorderPane borderPane;
    @FXML
    Text albumName;

    PictureService pictureService = new PictureService();
    AlbumService albumService = new AlbumService();
    Album album = new Album();
    ArrayList<Picture> selectedPhotos = new ArrayList<>();
    TilePane tilePane = new TilePane();
    List<VBox> pages = new ArrayList<>();

    private static double ELEMENT_SIZE = 170;
    private static final double GAP = ELEMENT_SIZE/10;

    public SecondaryController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        albumSetup();
        fillList();
        setupPicturePane();
        buttonSetup();
        createPdf();
        openMapView();
    }


    void buttonSetup() {
        if (album.getId() < 0) {
            vBox.getChildren().removeAll(addButton, deleteButton);
        }
    }

    void albumSetup() {
        album.setName(Context.getInstance().currentAlbum().getName());
        album.setId(Context.getInstance().currentAlbum().getId());
        albumName.setText(album.getName());
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

    void bind() {
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tilePane);
    }

    @FXML
    private void switchToPrimary() throws IOException {
        try{
            Context.getInstance().currentAlbum().setPictures(null);
            App.setRoot("primary");
        } catch (IOException e){
            AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
            AppLogger.closeHandler();
        }

    }


    public void setupPicturePane() {
        tilePane = elementPane();
        bind();
        pages = createPicturePages((ArrayList<Picture>) album.getPictures());
        setOnMouseClicked((ArrayList<Picture>) album.getPictures(), selectedPhotos, pages, "secondary");
        createElements(tilePane, pages);
    }


    public List<File> choosePictures() {
        final FileChooser dir = new FileChooser();
        List<String> filter = new ArrayList<>();
        Collections.addAll(filter, "*.jpg", "'.png", "*.jfif", "*.PNG");
        dir.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Pictures", filter));
        List<File> files = dir.showOpenMultipleDialog(vBox.getScene().getWindow());
        return files;
    }

    public void addPicture() {
        List<File> files = choosePictures();
        try {
            files.forEach(e -> {
                if (files.size() != 0) {
                    Picture p = null;
                    p = new Picture(e.getPath());
                    album.getPictures().add(p);
                    pictureService.createPicture(p, album.getId());
                    setupPicturePane();
                    ArrayList<Picture> pics = pictureService.getAllPictures(album.getId(), Context.getInstance().currentUser().getId());
                    album.setPictures(pics);
                }
            });
        } catch (NullPointerException e ) {
            AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
            AppLogger.closeHandler();
        }
    }



    void openMapView() {
        try{
            Image image = new Image(new FileInputStream("./images/globe.png"));
            mapViewIcon.setImage(image);
            mapViewIcon.setOnMouseClicked(e -> {
                Context.getInstance().currentAlbum().setPictures(album.getPictures());
                try {
                    App.setRoot("map");
                } catch (IOException ex) {
                    //AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
                    AppLogger.closeHandler();;
                }
            });
        } catch (FileNotFoundException e){
            AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
            AppLogger.closeHandler();
        }

    }

    void createPdf() {
        try{
            Image image = new Image(new FileInputStream("./images/pdf.png"));
            PdfHandler pdfHandler = new PdfHandler();
            pdfIcon.setImage(image);
            pdfIcon.setOnMouseClicked(e -> {
                if (selectedPhotos.size() > 0) {
                    pdfHandler.createAlbumPdf(selectedPhotos);
                }
            });
        } catch (FileNotFoundException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    public void deletePhotos(ActionEvent actionEvent) {
        if (selectedPhotos.size() > 0) {
            selectedPhotos.forEach(e -> pictureService.deletePicture(e.getId(), album.getId()));
            setupPicturePane();
        }
    }

    public void selectAll() {
        album.getPictures().forEach(x -> {
            if (!selectedPhotos.contains(x)) {
                selectedPhotos.add(x);
            }
        });
        pages.forEach(e -> e.setStyle("-fx-background-color:linear-gradient(white,#DDDDDD)"));
    }

    public void createAlbum(ActionEvent actionEvent) {
        TextInputDialog t = new TextInputDialog();
        t.setTitle("Album");
        t.setHeaderText("Create new album");
        t.setContentText("Enter name: ");
        Optional<String> result = t.showAndWait();
        if (result.isPresent()) {
            int userId = Context.getInstance().currentUser().getId();
            albumService.createAlbum(result.get(), userId);
            selectedPhotos.forEach(e -> pictureService.createPicture(e , albumService.getIdLastAlbumRegistered(userId)));
        }
    }

    public void sortIso(ActionEvent actionEvent) {
        album.sortIso();
        setupPicturePane();
    }

    public void sortFlash(ActionEvent actionEvent) {
        album.sortFlashUsed();
        setupPicturePane();
    }

    public void sortShutterSpeed(ActionEvent actionEvent) {
        album.sortShutterSpeed();
        setupPicturePane();
    }

    public void sortFileSize(ActionEvent actionEvent) {
        album.sortFileSize();
        setupPicturePane();
    }

    public void sortExposureTime(ActionEvent actionEvent) {
        album.sortExposureTime();
        setupPicturePane();
    }

    public void sortDate() {
        album.sortDate();
        setupPicturePane();
    }

    public void reverseOrder(ActionEvent actionEvent) {
        album.reverseOrder();
        setupPicturePane();
    }
}