package Team6;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.stream.Collectors;
import entities.Album;
import entities.Picture;
import entities.User;
import idk.AppLogger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import services.AlbumService;
import services.PictureService;
import services.UserService;


public class PrimaryController extends BaseController implements Initializable  {
    @FXML
    Button deleteButton;
    @FXML
    Button openButton;
    @FXML
    BorderPane borderPane;
    @FXML
    ScrollPane scrollPane;
    @FXML
    TextField textField;

    ArrayList<Album> yourAlbums = new ArrayList<>();
    ArrayList<Album> selectedAlbums = new ArrayList<>();
    AlbumService albumService = new AlbumService();
    PictureService pictureService = new PictureService();
    User user = Context.getInstance().currentUser();

    TilePane tilePane = new TilePane();
    List<VBox> pages = new ArrayList<>();

    public PrimaryController() throws IOException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupVariables();
        setupAlbumView();
        search();
        buttonSetup();
    }

    void buttonSetup() {
        if (selectedAlbums.size() <= 0) {
            openButton.setStyle("-fx-background-color: transparent");
            deleteButton.setStyle("-fx-background-color: transparent");
        } else {
            openButton.setStyle("-fx-background-color: white"); //linda problemet e nok her
            deleteButton.setStyle("-fx-background-color: white"); // teksten forsvinn osv, også veit æ ikke ka fargen hete
        }
    }


    public void setOnMouseClicked() {
        yourAlbums.forEach(a -> {
            pages.forEach(v -> {
                if (yourAlbums.indexOf(a) == pages.indexOf(v)) {
                    v.setOnMouseClicked(e -> {
                        if (selectedAlbums.contains(a)) {
                            v.setStyle("-fx-background-color: transparent");
                            selectedAlbums.remove(a);
                            buttonSetup();
                        } else {
                            v.setStyle("-fx-background-color:linear-gradient(white,#DDDDDD)");
                            selectedAlbums.add(a);
                            buttonSetup();
                        }
                        if (e.getClickCount() == 2) {
                            Context.getInstance().currentAlbum().setId(a.getId());
                            Context.getInstance().currentAlbum().setName(a.getName());
                            try {
                                switchScene("primary", "secondary");
                            } catch (IOException ex) {
                                AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
                                AppLogger.closeHandler();
                            }
                        }
                    });
                }
            });
        });
    }


    void setupAlbumView() {
        setOnMouseClicked();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tilePane);
    }

    void albumSetup() {
        yourAlbums = albumService.getAllAlbums(user.getId());
        Album album = new Album("All Photos");
        album.setId(-1);
        album.setUserId(Context.getInstance().currentUser().getId());
        yourAlbums.add(0, album);
    }

    public void setupVariables() {
        albumSetup();
        tilePane = elementPane();
        pages = createAlbumPages(yourAlbums, "./images/icon_2.png");
        createElements(tilePane, pages);
    }


    public void createAlbum(ActionEvent actionEvent) {
        TextInputDialog t = new TextInputDialog();
        t.setTitle("Album");
        t.setHeaderText("Create new album");
        t.setContentText("Enter name: ");
        Optional<String> result = t.showAndWait();
        if (result.isPresent()) {
            albumService.createAlbum(result.get(), user.getId());
            setupVariables();
            setupAlbumView();
        }
    }

    public void deleteAlbum(ActionEvent actionEvent) {
        if (selectedAlbums.size() > 0) {
            selectedAlbums.forEach(e -> {
                e.setPictures(pictureService.getAllPictures(e.getId(), Context.getInstance().currentUser().getId()));
                albumService.deleteAlbum(e);
                yourAlbums.remove(e);
            });
            setupVariables();
            setupAlbumView();
        }
    }

    public void openAlbum() throws IOException {
        try {
            if (chosenOnes.size() == 1) {
                Album album = chosenOnes.get(0);
                Context.getInstance().currentAlbum().setId(album.getId());
                Context.getInstance().currentAlbum().setName(album.getName());
                switchScene("primary", "secondary");
            }
        } catch (IOException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    public void search() {
        textField.setOnMouseClicked(e -> {
            try {
                switchScene("primary", "search");
            } catch (IOException ex) {
                AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
                AppLogger.closeHandler();
            }
        });
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        switchScene("primary", "login");
    }
}
