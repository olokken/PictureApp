package Team6;

import entities.Picture;
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
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SearchController implements Initializable {
    private final double ELEMENT_SIZE = 170;
    private final double GAP = ELEMENT_SIZE/10;
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

    //Create logger object from PicLdLogger class.
    //private PicLdLogger picLdLogger = new PicLdLogger();

    public SearchController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Context.getInstance().setSwitchToMap(false);
        bind();
        rememberLastSearch();
        search();
    }

    void rememberLastSearch() {
        if (Context.getInstance().currentSearchingword() != null) {
            String lastSearched = Context.getInstance().currentSearchingword();
            addPictures(lastSearched);
            textField.setText(lastSearched);
            createElements();
        }
    }
    void bind() {
        tilePane.setHgap(GAP);
        tilePane.setVgap(GAP);
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
                    createElements();
            }
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

    void createElements() {
        tilePane.getChildren().clear();
        tilePane.getChildren().addAll(createPages());
    }

    List<ImageView> createImageViews() {
        return searchedPictures.stream().map(x -> {
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
                if (e.getClickCount() == 2) {
                    Context.getInstance().setCurrentSearchingword(textField.getText());
                    Context.getInstance().currentAlbum().setPictures(searchedPictures);
                    Context.getInstance().currentAlbum().setId(-2);
                    Context.getInstance().setIndex(searchedPictures.indexOf(x));
                    try {
                        App.setRoot("tertiary");
                    } catch (IOException ex) {
                        //picLdLogger.getLogger().log(Level.FINE, ex.getMessage());
                    }
                }
            });
            return imageView;
        }).collect(Collectors.toList());
    }

    List<VBox> createPages() {
        List<VBox> pages = createImageViews().stream().map(x -> {
            VBox vBox = new VBox();
            vBox.setPadding(new Insets(3,3,3,3));
            vBox.getChildren().add(x);
            return vBox;
            }).collect(Collectors.toList());
        pages.forEach(x -> {
            x.setOnMouseClicked(e -> {
                int index = pages.indexOf(x);
                Picture picture = searchedPictures.get(index);
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
        searchedPictures.forEach(x -> selectedPhotos.add(x));
        createPages().forEach(x -> x.setStyle("-fx-background-color: green"));
    }



    public void switchToPrimary(ActionEvent actionEvent) throws IOException {
        Context.getInstance().currentAlbum().setPictures(null);
        App.setRoot("primary");
    }
}
