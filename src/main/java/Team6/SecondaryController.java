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
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import services.AlbumService;
import services.PdfHandler;
import services.PictureService;



public class SecondaryController implements Initializable {
    @FXML
    ImageView pdfIcon;
    @FXML
    Button createAlbumButton;
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
    AlbumService albumService = new AlbumService();
    Album album = new Album();
    TilePane tilePane = new TilePane();
    List<VBox> pages;
    ArrayList<Picture> selectedPhotos = new ArrayList<>();

    private static double ELEMENT_SIZE = 170;
    private static final double GAP = ELEMENT_SIZE/10;

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
            pdfSetup();
            mapViewSetup();
        } catch (FileNotFoundException e) {
            AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
            AppLogger.closeHandler();
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
        try{
            Context.getInstance().currentAlbum().setPictures(null);
            App.setRoot("primary");
        } catch (IOException e){
            AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
            AppLogger.closeHandler();
        }

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
                AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
                AppLogger.closeHandler();
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(ELEMENT_SIZE);
            imageView.setFitWidth(ELEMENT_SIZE);
            imageView.setSmooth(true);
            imageView.setCache(true);
            imageView.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    Context.getInstance().currentAlbum().setPictures(album.getPictures());
                    Context.getInstance().currentAlbum().setId(album.getId());
                    Context.getInstance().setIndex(album.getPictures().indexOf(x));
                    Context.getInstance().setLastScene("secondary");
                    try {
                        App.setRoot("tertiary");
                    }   catch (IOException ex) {
                        AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
                        AppLogger.closeHandler();
                    }
                }
            });
            return imageView;
        }).collect(Collectors.toList());
    }

    List<VBox> createPages() {
            pages = createImageViews().stream().map(x -> {
                VBox vBox = new VBox();
                vBox.setPadding(new Insets(3,3,3,3));
                vBox.getChildren().add(x);
                return vBox;
            }).collect(Collectors.toList());

            pages.forEach(x -> {
                x.setOnMouseClicked(e -> {
                    int index = pages.indexOf(x);
                    Picture picture = album.getPictures().get(index);
                    if (!selectedPhotos.contains(picture)) {
                        selectedPhotos.add(picture);
                        x.setStyle("-fx-background-color: green");
                    } else {
                        selectedPhotos.remove(picture);
                        x.setStyle("-fx-background-color: white");
                    }
                });
            });
            return pages;
        }


    public void sortIso(ActionEvent actionEvent) {
        album.sortIso();
        createElements();
    }

    public void sortFlash(ActionEvent actionEvent) {
        album.sortFlashUsed();
        createElements();
    }

    public void sortShutterSpeed(ActionEvent actionEvent) {
        album.sortShutterSpeed();
        createElements();
    }

    public void sortFileSize(ActionEvent actionEvent) {
        album.sortFileSize();
        createElements();
    }

    public void sortExposureTime(ActionEvent actionEvent) {
        album.sortExposureTime();
        createElements();
    }

    public void sortDate() {
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
                    p = new Picture(e.getPath());
                    album.getPictures().add(p);
                    pictureService.createPicture(p, album.getId());
                    createElements();
                    ArrayList<Picture> pics = pictureService.getAllPictures(album.getId(), Context.getInstance().currentUser().getId());
                    album.setPictures(pics);
                }
            });
        } catch (NullPointerException e ) {
            AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
            AppLogger.closeHandler();
        }
    }

    public void reverseOrder(ActionEvent actionEvent) {
        album.reverseOrder();
        createElements();
    }

    void mapViewSetup() throws FileNotFoundException {
        try{
            Image image = new Image(new FileInputStream(".\\images\\globe.png"));
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

    void pdfSetup() throws FileNotFoundException {
        try{
            Image image = new Image(new FileInputStream(".\\images\\pdf.png"));
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
            createElements();
        }
    }

    public void selectAll() {
        album.getPictures().forEach(x -> {
            if (!selectedPhotos.contains(x)) {
                selectedPhotos.add(x);
            }
        });
        pages.forEach(e -> e.setStyle("-fx-background-color: green"));
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


}