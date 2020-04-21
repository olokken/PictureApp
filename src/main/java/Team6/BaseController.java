package Team6;

import entities.Album;
import entities.Picture;
import idk.AppLogger;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BaseController {

    public void switchScene(String currentScene, String nextScene) throws IOException {
        try {
            Context.getInstance().setLastScene(currentScene);
            App.setRoot(nextScene);
        } catch (IOException e) {
            AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
            AppLogger.closeHandler();
        }
    }

    public ImageView createImageView (String filePath, int elementSize) throws FileNotFoundException {
        Image image = null;
        image = new Image(new FileInputStream(filePath)); //filePath
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(elementSize);
        imageView.setFitWidth(elementSize);
        imageView.setSmooth(true);
        imageView.setCache(true);
        return imageView;
    }

    public VBox createVBoxOptions(int padding, String imagePath, int elementSize) throws FileNotFoundException {
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: transparent");
        vBox.setPadding(new Insets(10,10,10,10));
        vBox.getChildren().add(createImageView(imagePath, elementSize));
        return vBox;
    }

    public List<VBox> createAlbumPages (ArrayList<Album> yourAlbums, String iconPath) {
        return yourAlbums.stream().map(x -> {
            VBox vBox = null;
            Text text = new Text(x.getName());
            text.setTextAlignment(TextAlignment.CENTER);
            try {
                text.setWrappingWidth(createImageView(iconPath, 80).getFitWidth());
                vBox = createVBoxOptions(10 , iconPath, 80);
            } catch (FileNotFoundException e) {
                AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
                AppLogger.closeHandler();
            }
            vBox.getChildren().add(text);
            return vBox;
        }).collect(Collectors.toList());
    }

    public List<VBox> createPicturePages (ArrayList<Picture> pictures) {
        return pictures.stream().map(x -> {
            VBox vBox = null;
            try { ;
                vBox = createVBoxOptions(10 , x.getFilepath(), 170);
            } catch (FileNotFoundException e) {
                AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
                AppLogger.closeHandler();
            }
            return vBox;
        }).collect(Collectors.toList());
    }

    public TilePane elementPane() {
        TilePane tilePane = new TilePane();
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        return tilePane;
    }

    public void createElements(TilePane tilePane, List<VBox> pages) {
        tilePane.getChildren().clear();
        tilePane.getChildren().addAll(pages);
    }

    public void setOnMouseClicked(ArrayList<Picture> pictures, ArrayList<Picture> selectedPictures, List<VBox> pages, String currentScene) {
        pictures.forEach(a -> {
            pages.forEach(v -> {
                if (pictures.indexOf(a) == pages.indexOf(v)) {
                    v.setOnMouseClicked(e -> {
                        if (selectedPictures.contains(a)) {
                            v.setStyle("-fx-background-color: transparent");
                            selectedPictures.remove(a);
                        } else {
                            v.setStyle("-fx-background-color:linear-gradient(white,#DDDDDD)");
                            selectedPictures.add(a);
                        }
                        if (e.getClickCount() == 2) {
                            Context.getInstance().currentAlbum().setPictures(pictures);
                            Context.getInstance().setIndex(pictures.indexOf(a));
                            try {
                                switchScene(currentScene, "tertiary");
                            } catch (IOException ex) {
                                //picLdLogger.getLogger().log(Level.FINE, ex.getMessage());
                            }
                        }

                    });
                }
            });
        });
    }

}
