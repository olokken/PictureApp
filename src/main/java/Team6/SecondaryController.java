package Team6;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import entities.Album;
import entities.Picture;
import idk.AppLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    Button createPdfButton;
    @FXML
    VBox vBox;
    @FXML
    Button deleteButton;
    @FXML
    Button addButton;
    @FXML
    MenuButton changeStatusButton;
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


    public SecondaryController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        albumSetup();
        fillList();
        setupPicturePane();
        buttonSetup();
        deleteButtonSetup();
        addListener();
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
            ArrayList<Picture> pics = (ArrayList<Picture>) pictureService.getAllPictures(album.getId(), Context.getInstance().currentUser().getId());
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
            switchScene("secondary", "primary");
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
        return dir.showOpenMultipleDialog(vBox.getScene().getWindow());
    }

    public void addPicture() {
        List<File> files = choosePictures();
        try {
            files.forEach(e -> {
                if (!files.isEmpty()) {
                    Picture p = null;
                    p = new Picture(e.getPath());
                    album.getPictures().add(p);
                    pictureService.createPicture(p, album.getId());
                    ArrayList<Picture> pics = (ArrayList<Picture>) pictureService.getAllPictures(album.getId(), Context.getInstance().currentUser().getId());
                    album.setPictures(pics);
                    setupPicturePane();
                }
            });
        } catch (NullPointerException e ) {
            AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
            AppLogger.closeHandler();
        }
    }

    void deleteButtonSetup() {
        if (selectedPhotos.isEmpty()) {
            deleteButton.setStyle("-fx-background-color: transparent");
        } else {
             deleteButton.setStyle("-fx-background-color: linear-gradient(to bottom,#3F3F3F,#2B2B2B)");
        }
    }

    void addListener() {
        pages.forEach(x -> {
            x.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent ->  deleteButtonSetup());
        });
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
                    AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
                    AppLogger.closeHandler();
                }
            });
        } catch (FileNotFoundException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    @FXML
    void createPdf() {
        PdfHandler pdfHandler = new PdfHandler();
        if (selectedPhotos.isEmpty()) {
            pdfHandler.createPdfAlbum(album.getPictures());
            showInformationDialog("PDF created", "You have created a pdf file with all your album pictures, which is located in download");
        } else {
            pdfHandler.createPdfAlbum(selectedPhotos);
            showInformationDialog("PDF created", "You have created a pdf file with all your selected pictures, which is located in download");
        }
    }

    public void deletePhotos() {
        if (!selectedPhotos.isEmpty()) {
            selectedPhotos.forEach(e -> pictureService.deletePicture(e.getId(), album.getId()));
            ArrayList<Picture> pics = (ArrayList<Picture>) pictureService.getAllPictures(album.getId(), Context.getInstance().currentUser().getId());
            album.setPictures(pics);
            setupPicturePane();
        }
    }

    public void selectAll() {
        selectAll(album.getPictures(), selectedPhotos, pages);
    }

    public void createAlbum() {
        Optional<String> result = showInputDialog("Create new album", "Enter name :");
        if (result.isPresent()) {
            int userId = Context.getInstance().currentUser().getId();
            albumService.createAlbum(result.get(), userId);
            selectedPhotos.forEach(e -> pictureService.createPicture(e , albumService.getLastAlbumIdRegistered(userId)));
        }
    }

    public void sortIso() {
        album.sortIso();
        setupPicturePane();
    }

    public void sortFlash() {
        album.sortFlashUsed();
        setupPicturePane();
    }

    public void sortShutterSpeed() {
        album.sortShutterSpeed();
        setupPicturePane();
    }

    public void sortFileSize() {
        album.sortFileSize();
        setupPicturePane();
    }

    public void sortExposureTime() {
        album.sortExposureTime();
        setupPicturePane();
    }

    public void sortDate() {
        album.sortDate();
        setupPicturePane();
    }

    public void reverseOrder() {
        album.reverseOrder();
        setupPicturePane();
    }
}