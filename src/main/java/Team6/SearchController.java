package Team6;

import entities.Picture;
import idk.AppLogger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import services.AlbumService;
import services.PictureService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SearchController extends BaseController implements Initializable {
    @FXML
    TextField textField;
    @FXML
    ScrollPane scrollPane;

    PictureService pictureService = new PictureService();
    AlbumService albumService = new AlbumService();
    ArrayList<Picture> pictures = pictureService.getAllPictures(-1, Context.getInstance().currentUser().getId());
    ArrayList<Picture> searchedPictures = new ArrayList<>();
    ArrayList<Picture> selectedPhotos = new ArrayList<>();

    TilePane tilePane = new TilePane();
    List<VBox> pages = new ArrayList<>();

    public SearchController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bind();
        rememberLastSearch();
        search();
    }

    void rememberLastSearch() {
        if (Context.getInstance().currentSearchingword() != null) {
            String lastSearched = Context.getInstance().currentSearchingword();
            addPictures(lastSearched);
            textField.setText(lastSearched);
            setupPicturePane();
        }
    }

    void bind() {
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tilePane);
    }

    void search () {
        textField.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER:
                    searchedPictures.clear();
                    tilePane.getChildren().remove(searchedPictures);
                    addPictures(textField.getText());
                    setupPicturePane();
            }
        });
    }

    public void setOnMouseClicked() {
        searchedPictures.forEach(a -> {
            pages.forEach(v -> {
                if (searchedPictures.indexOf(a) == pages.indexOf(v)) {
                    v.setOnMouseClicked(e -> {
                        if (selectedPhotos.contains(a)) {
                            v.setStyle("-fx-background-color: transparent");
                            selectedPhotos.remove(a);
                        } else {
                            v.setStyle("-fx-background-color:linear-gradient(white,#DDDDDD)");
                            selectedPhotos.add(a);
                        }
                        if (e.getClickCount() == 2) {
                            Context.getInstance().currentAlbum().setPictures(searchedPictures);
                            Context.getInstance().setIndex(searchedPictures.indexOf(a));
                            Context.getInstance().setCurrentSearchingword(textField.getText());
                            try {
                                switchScene("search", "tertiary");
                            } catch (IOException ex) {
                                //picLdLogger.getLogger().log(Level.FINE, ex.getMessage());
                            }
                        }

                    });
                }
            });
        });
    }


    void addPictures(String searchingWord) {
        pictures.forEach(x -> {
            Optional<String> tag = x.getTags().stream().filter(e -> e.equalsIgnoreCase(searchingWord)).findAny();
            String[] fileName = x.getFileName().split("\\.");
            if (tag.isPresent() || searchingWord.equalsIgnoreCase(fileName[0])) {
                if (!searchedPictures.contains(x) || searchingWord.equalsIgnoreCase(fileName[0])){
                    searchedPictures.add(x);
                }
            }
        });
    }

    void setupPicturePane() {
        tilePane = elementPane();
        bind();
        pages = createPicturePages(searchedPictures);
        setOnMouseClicked();
        createElements(tilePane, pages);
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

    public void selectAll() {
        searchedPictures.forEach(x -> {
            if (!selectedPhotos.contains(x)) {
                selectedPhotos.add(x);
            }
        });
        pages.forEach(e -> e.setStyle("-fx-background-color: green"));
    }

    public void switchToPrimary(ActionEvent actionEvent) throws IOException {
        try{
            Context.getInstance().currentAlbum().setPictures(null);
            App.setRoot("primary");
        } catch (IOException e){
            AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
            AppLogger.closeHandler();
        }
    }
}
