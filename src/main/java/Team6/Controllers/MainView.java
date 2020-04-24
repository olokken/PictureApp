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


/**
 * Controller for MainView.
 *
 * @author Team 6
 * @version 2020.04.24
 */
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

    /**
     * Constructor that creates an instance of MainView, initialising the instance.
     */
    public MainView() throws IOException {}

    /**
     * Initialize the main view.
     *
     * @param url The url.
     * @param resourceBundle The resource bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupVariables();
        setupAlbumView();
        search();
        setupButtonCss();
    }

    /**
     * Sets color to the button if album is selected.
     */
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

    /**
     * Adds an event to album so you can change scene by clicking it.
     */
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

    /**
     * Sets up the albums view.
     */
    void setupAlbumView() {
        setOnMouseClicked();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tilePane);
    }

    /**
     * Sets up all the albums.
     */
    void setupAlbums() {
        yourAlbums = (ArrayList<Album>) albumService.getAllAlbums(user.getId());
        Album album = new Album("All Photos");
        album.setId(-1);
        album.setUserId(Context.getInstance().currentUser().getId());
        yourAlbums.add(0, album);
    }

    /**
     * Sets up the albums icons.
     */
    public void setupVariables() {
        setupAlbums();
        tilePane = elementPane();
        pages = createAlbumPages(yourAlbums, "./images/icon_green.png");
        createElements(tilePane, pages);
    }

    /**
     * Initialize create album button to create an album.
     */
    public void createAlbum() {
        Optional<String> result = showInputDialog("Create new album", "Enter name :");
        if (result.isPresent()) {
            albumService.createAlbum(result.get(), user.getId());
            setupVariables();
            setupAlbumView();
        }
    }

    /**
     * Initialize delete album button to delete chosen album(s).
     */
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

    /**
     * Initialize open album button to change scene to album view.
     */
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

    /**
     * Initialize search button to switch scene to search view.
     */
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

    /**
     * Initialize log out button to switch scene to login view.
     */
    public void logOut() throws IOException {
        switchScene("MainView", "LoginView");
    }
}
