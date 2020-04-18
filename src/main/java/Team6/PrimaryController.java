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


public class PrimaryController implements Initializable  {
    @FXML
    BorderPane borderPane;
    @FXML
    ScrollPane scrollPane;
    @FXML
    TextField textField;
    @FXML
    AnchorPane anchorPane;

    TilePane tilePane = new TilePane();

    ArrayList<Album> yourAlbums = new ArrayList<Album>();
    ArrayList<Album> chosenOnes = new ArrayList<Album>();
    AlbumService albumService = new AlbumService();
    PictureService pictureService = new PictureService();
    UserService userService = new UserService();
    User user = Context.getInstance().currentUser();

    private static double ELEMENT_SIZE = 100;
    private static final double GAP = ELEMENT_SIZE/10;

    //Create logger object from PicLdLogger class.
    //private PicLdLogger picLdLogger = new PicLdLogger();

    public PrimaryController() throws IOException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        albumViewSetup();
        fillList();
        search();
    }

    void createElements() {
        tilePane.getChildren().clear();
        tilePane.getChildren().addAll(createPages());
    }

    List<VBox> createPages() {
        return yourAlbums.stream().map(x -> {
            VBox vBox = new VBox();
            vBox.setStyle("-fx-background-color: transparent");
            Text text = new Text(x.getName());
            text.setTextAlignment(TextAlignment.CENTER);
            vBox.setPadding(new Insets(10,10,10,10));
            try {
                vBox.getChildren().add(createImageView());
                text.setWrappingWidth(createImageView().getFitWidth());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            vBox.getChildren().add(text);
            //vBox.setOnMouseDragOver


            vBox.setOnMouseClicked(e -> {
                if(chosenOnes.contains(x)) {
                    vBox.getStyleClass().add("button1");
                    chosenOnes.remove(x);
                }
                else {
                    vBox.setStyle("-fx-background-color:linear-gradient(white,#DDDDDD)");
                    chosenOnes.add(x);
                }
                if (e.getClickCount() == 2) {
                    Context.getInstance().currentAlbum().setId(x.getId());
                    Context.getInstance().currentAlbum().setName(x.getName());
                    try {
                        App.setRoot("secondary");
                    } catch (IOException ex) {
                        //picLdLogger.getLogger().log(Level.FINE, ex.getMessage());
                    }
                }
            });
            return vBox;
        }).collect(Collectors.toList());
    }

    ImageView createImageView() throws FileNotFoundException {
        Image image = null;
        image = new Image(new FileInputStream(".\\images\\icon_2.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        //imageView.setSmooth(true);
        imageView.setCache(true);
        return imageView;
    }

    void albumViewSetup() {
        tilePane.setHgap(GAP);
        tilePane.setVgap(GAP);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tilePane);
    }

    public void fillList() {
        yourAlbums = albumService.getAllAlbums(user.getId());
        Album album = new Album("All Photos");
        album.setId(-1);
        album.setUserId(Context.getInstance().currentUser().getId());
        yourAlbums.add(0, album);
        createElements();
    }


    public void createAlbum(ActionEvent actionEvent) throws IOException {
        TextInputDialog t = new TextInputDialog();
        t.setTitle("Album");
        t.setHeaderText("Create new album");
        t.setContentText("Enter name: ");
        Optional<String> result = t.showAndWait();
        if (result.isPresent()) {
            albumService.createAlbum(result.get(), user.getId());
            fillList();
        }
    }

    public void deleteAlbum(ActionEvent actionEvent) {
        if (chosenOnes.size() > 0) {
            chosenOnes.forEach(e -> {
                e.setPictures(pictureService.getAllPictures(e.getId(), Context.getInstance().currentUser().getId()));
                albumService.deleteAlbum(e);
                yourAlbums.remove(e);
            });
            createElements();
        }
    }

    public void openAlbum() throws IOException {
        if (chosenOnes.size() == 1) {
            Album album = chosenOnes.get(0);
            Context.getInstance().currentAlbum().setId(album.getId());
            Context.getInstance().currentAlbum().setName(album.getName());
            App.setRoot("secondary");
        }
    }

    public void search() {
        textField.setOnMouseClicked(e -> {
            try {
                App.setRoot("search");
            } catch (IOException ex) {
                //picLdLogger.getLogger().log(Level.FINE, ex.getMessage());
            }
        });
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        App.setRoot("login");
    }
}
