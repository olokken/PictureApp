package Team6;

import entities.Album;
import entities.Picture;
import idk.AppLogger;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BaseController {

    public void switchScene(String currentScene, String nextScene) throws IOException {
        try {
            Context.getInstance().setLastScene(currentScene);
            App.setRoot(nextScene);
        } catch (IOException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    public ImageView createImageView (String filePath, int elementSize) throws FileNotFoundException {
        try{
            Image image = null;
            ImageView imageView = new ImageView();
            File file = new File(filePath);
            imageView.setFitHeight(elementSize);
            imageView.setFitWidth(elementSize);
            imageView.setSmooth(true);
            imageView.setCache(true);
            if(file.exists()) {
                image = new Image(new FileInputStream(filePath)); //filePath
                imageView.setImage(image);
            } else {
                image = new Image(new FileInputStream("./images/notAvailable.png"));
                imageView.setImage(image);
            }
            return imageView;
        } catch (FileNotFoundException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
            return null;
        }
    }

    public VBox createVBoxOptions(int padding, String imagePath, int elementSize) throws FileNotFoundException {
        try{
            VBox vBox = new VBox();
            vBox.setStyle("-fx-background-color: transparent");
            vBox.setPadding(new Insets(padding,padding,padding,padding));
            vBox.getChildren().add(createImageView(imagePath, elementSize));
            return vBox;
        } catch (FileNotFoundException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
            return null;
        }
    }

    public List<VBox> createAlbumPages (ArrayList<Album> yourAlbums, String iconPath) {
        return yourAlbums.stream().map(x -> {
            VBox vBox = null;
            Text text = new Text(x.getName());
            text.setTextAlignment(TextAlignment.CENTER);
            try {
                text.setWrappingWidth(createImageView(iconPath, 80).getFitWidth());
                vBox = createVBoxOptions(10 , iconPath, 80);
            } catch (FileNotFoundException ex) {
                AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
                AppLogger.closeHandler();
            }
            vBox.getChildren().add(text);
            return vBox;
        }).collect(Collectors.toList());
    }

    public List<VBox> createPicturePages (ArrayList<Picture> pictures) {
        return pictures.stream().map(x -> {
            VBox vBox = null;
            try {
                vBox = createVBoxOptions(5 , x.getFilepath(), 170);
            } catch (FileNotFoundException ex) {
                AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
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
                    v.setOnMousePressed(e -> {
                        if (selectedPictures.contains(a)) {
                            v.setStyle("-fx-background-color: transparent");
                            selectedPictures.remove(a);
                        } else {
                            v.setStyle("-fx-background-color:linear-gradient(black,#DDDDDD)");
                            selectedPictures.add(a);
                        }
                        if (e.getClickCount() == 2) {
                            Context.getInstance().currentAlbum().setPictures(pictures);
                            Context.getInstance().setIndex(pictures.indexOf(a));
                            try {
                                switchScene(currentScene, "tertiary");
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

    public void selectAll(List<Picture> pictures, List<Picture> selectedPictuers, List<VBox> pages) {
        pictures.forEach(x -> {
            if (!selectedPictuers.contains(x)) {
                selectedPictuers.add(x);
            }
        });
        pages.forEach(e -> e.setStyle("-fx-background-color:linear-gradient(black,#DDDDDD)"));
    }

    public Optional<String> showInputDialog (String header, String content) {
        TextInputDialog t = new TextInputDialog();
        t.setTitle("PicLd");
        t.setHeaderText("Create new album");
        t.setContentText("Enter name: ");
        return t.showAndWait();
    }

    public void showInformationDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
