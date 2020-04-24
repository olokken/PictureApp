package Team6.Controllers;

import Team6.entities.Picture;
import Team6.services.AppLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import Team6.services.AlbumService;
import Team6.services.PictureService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * Controller for SearchView.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class SearchView extends Base implements Initializable {
    @FXML
    TextField textField;
    @FXML
    ScrollPane scrollPane;

    PictureService pictureService = new PictureService();
    AlbumService albumService = new AlbumService();
    ArrayList<Picture> pictures = (ArrayList<Picture>) pictureService.getAllPictures(-1, Context.getInstance().currentUser().getId());
    ArrayList<Picture> searchedPictures = new ArrayList<>();
    ArrayList<Picture> selectedPhotos = new ArrayList<>();

    TilePane tilePane = new TilePane();
    List<VBox> pages = new ArrayList<>();

    /**
     * Constructor that creates an instance of SearchView, initialising the instance.
     */
    public SearchView() throws IOException {
    }

    /**
     * Initialize the SearchView.
     *
     * @param url The url.
     * @param resourceBundle The resource bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bind();
        rememberLastSearch();
        search();
    }

    /**
     * So the searched pictures don't disappear when you switch to
     * PictureView and back.
     */
    void rememberLastSearch() {
        if (Context.getInstance().currentSearchingword() != null) {
            String lastSearched = Context.getInstance().currentSearchingword();
            addPictures(lastSearched);
            textField.setText(lastSearched);
            setupPicturePane();
        }
    }

    /**
     * Binds scrollPane to the tilePane, so you can scroll the tilePane.
     */
    void bind() {
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tilePane);
    }

    /**
     * Search that show the pictures with the searched tag.
     */
    void search () {
        textField.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER:
                    searchedPictures.clear();
                    tilePane.getChildren().remove(searchedPictures);
                    addPictures(textField.getText());
                    Context.getInstance().setCurrentSearchingword(textField.getText());
                    setupPicturePane();
                default:
            }
        });
    }

    /**
     * Adds picture to a ArrayList contaning the given search word.
     * @param searchWord The search word.
     */
    void addPictures(String searchWord) {
        pictures.forEach(x -> {
            Optional<String> tag = x.getTags().stream().filter(e -> e.equalsIgnoreCase(searchWord)).findAny();
            String[] fileName = x.getFileName().split("\\.");
            if (tag.isPresent() || searchWord.equalsIgnoreCase(fileName[0])) {
                if (!searchedPictures.contains(x) || searchWord.equalsIgnoreCase(fileName[0])){
                    searchedPictures.add(x);
                }
            }
        });
    }

    /**
     * Sets up picturePane.
     */
    public void setupPicturePane() {
        tilePane = elementPane();
        bind();
        pages = createPicturePages(searchedPictures);
        setOnMouseClicked(searchedPictures, searchedPictures, pages, "SearchView");
        createElements(tilePane, pages);
    }

    /**
     * Initialising create album button to create an album.
     */
    public void createAlbum() {
        Optional<String> result = showInputDialog("Create new album", "Enter name :");
        if (result.isPresent()) {
            if(!selectedPhotos.isEmpty()) {
                int userId = Context.getInstance().currentUser().getId();
                albumService.createAlbum(result.get(), userId);
                selectedPhotos.forEach(e -> pictureService.createPicture(e , albumService.getIdLastAlbumRegistered(userId)));
            } else {
                showInformationDialog("No selected pictures", "You have to select which pictures that you want in your new album");
            }

        }
    }

    /**
     * Selects all searched pictures and adding them to
     * the selectedPhotos ArrayList.
     */
    public void selectAll() {
        selectAll(searchedPictures, selectedPhotos, pages);
    }

    /**
     * Switches from SearchView to MainView.
     */
    public void switchToMainView() {
        try{
            Context.getInstance().currentAlbum().setPictures(null);
            switchScene("SearchView", "MainView");
        } catch (IOException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }
}
