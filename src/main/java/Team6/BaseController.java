package Team6;

import entities.Album;
import entities.Picture;
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
import java.util.stream.Collectors;

public class BaseController {

    public void switchScene(String currentScene, String nextScene) throws IOException {
        Context.getInstance().setLastScene(currentScene);
        App.setRoot(nextScene);
    }

    public ImageView createImageView (String filePath) throws FileNotFoundException {
        Image image = null;
        image = new Image(new FileInputStream(filePath)); //filePath
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        imageView.setSmooth(true);
        imageView.setCache(true);
        return imageView;
    }

    public VBox createVBoxOptions(int padding, String imagePath) throws FileNotFoundException {
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: transparent");
        vBox.setPadding(new Insets(10,10,10,10));
        vBox.getChildren().add(createImageView(imagePath));
        return vBox;
    }

    public List<VBox> createAlbumPages (ArrayList<Album> yourAlbums) {
        return yourAlbums.stream().map(x -> {
            VBox vBox = null;
            Text text = new Text(x.getName());
            text.setTextAlignment(TextAlignment.CENTER);
            try {
                text.setWrappingWidth(createImageView(".\\images\\icon_2.png").getFitWidth());
                vBox = createVBoxOptions(10 , ".\\images\\icon_2.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            vBox.getChildren().add(text);
            return vBox;
        }).collect(Collectors.toList());
    }

    public List<VBox> createPicturePages (ArrayList<Picture> pictures) {
        return pictures.stream().map(x -> {
            VBox vBox = null;
            try { ;
                vBox = createVBoxOptions(10 , x.getFilepath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
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

}
