package Team6.Controllers;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import Team6.entities.Album;
import Team6.entities.User;
import Team6.services.AppLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import Team6.services.AlbumService;
import Team6.services.PictureService;


public class MainView extends Base implements Initializable  {
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
    String transparentButton = "-fx-background-color: linear-gradient(to bottom,#353535,#3D3C3C)";
    String buttonColor = "-fx-background-color: linear-gradient(to bottom,#1D1D1D,#2B2B2B)";

    TilePane tilePane = new TilePane();
    List<VBox> pages = new ArrayList<>();

    public MainView() throws IOException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupVariables();
        setupAlbumView();
        search();
        setupButtonCss();
    }

    void setupButtonCss() {
        if (selectedAlbums.isEmpty()) {
            openButton.setStyle("-fx-background-color: #2e2d2d; -fx-text-fill: #4d4d4d");
            deleteButton.setStyle("-fx-background-color: #2e2d2d; -fx-text-fill: #4d4d4d");
        } else {
            openButton.setStyle(null);
            deleteButton.setStyle(null);
            openButton.getStyleClass().add("MainView.css");
            deleteButton.getStyleClass().add("MainView.css");
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
                            setupButtonCss();
                        } else {
                            v.setStyle("-fx-background-color: #AAAAAA");
                            selectedAlbums.add(a);
                            setupButtonCss();
                        }
                        if (e.getClickCount() == 2) {
                            Context.getInstance().currentAlbum().setId(a.getId());
                            Context.getInstance().currentAlbum().setName(a.getName());
                            try {
                                switchScene("MainView", "AlbumView");
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

    void setupAlbums() {
        yourAlbums = (ArrayList<Album>) albumService.getAllAlbums(user.getId());
        Album album = new Album("All Photos");
        album.setId(-1);
        album.setUserId(Context.getInstance().currentUser().getId());
        yourAlbums.add(0, album);
    }

    public void setupVariables() {
        setupAlbums();
        tilePane = elementPane();
        pages = createAlbumPages(yourAlbums, "./images/icon_green.png");
        createElements(tilePane, pages);
    }


    public void createAlbum() {
        Optional<String> result = showInputDialog("Create new album", "Enter name :");
        if (result.isPresent()) {
            albumService.createAlbum(result.get(), user.getId());
            setupVariables();
            setupAlbumView();
        }
    }

    public void deleteAlbum() {
        if (!selectedAlbums.isEmpty()) {
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
            if (selectedAlbums.size() == 1) {
                Album album = selectedAlbums.get(0);
                Context.getInstance().currentAlbum().setId(album.getId());
                Context.getInstance().currentAlbum().setName(album.getName());
                switchScene("MainView", "AlbumView");
            }
        } catch (IOException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    public void search() {
        textField.setOnMouseClicked(e -> {
            try {
                switchScene("MainView", "SearchView");
            } catch (IOException ex) {
                AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
                AppLogger.closeHandler();
            }
        });
    }

    public void logOut() throws IOException {
        switchScene("MainView", "LoginView");
    }
}
