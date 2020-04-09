package Team6;

import entities.Picture;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import services.PictureService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SearchController implements Initializable {
    private final double ELEMENT_SIZE = 170;
    private final double GAP = ELEMENT_SIZE/10;
    @FXML
    TextField textField;
    @FXML
    ScrollPane scrollPane;
    PictureService pictureService = new PictureService();
    ArrayList<Picture> pictures = pictureService.getAllPictures(0);
    ArrayList<Picture> searchedPictures = new ArrayList<>();
    TilePane tilePane = new TilePane();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bind();
        search();
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
            if (tag.isPresent()) {
                if (!searchedPictures.contains(x)){
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
                e.printStackTrace();
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(ELEMENT_SIZE);
            imageView.setFitWidth(ELEMENT_SIZE);
            imageView.setSmooth(true);
            imageView.setCache(true);
            imageView.setOnMouseClicked(e -> {
                Context.getInstance().currentAlbum().setPictures(searchedPictures);
                Context.getInstance().currentAlbum().setId(-1);
                Context.getInstance().setIndex(searchedPictures.indexOf(x));
                try {
                    App.setRoot("tertiary");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            return imageView;
        }).collect(Collectors.toList());
    }

    List<VBox> createPages() {
        return createImageViews().stream().map(x -> {
            VBox vBox = new VBox();
            vBox.getChildren().add(x);
            return vBox;
        }).collect(Collectors.toList());
    }}