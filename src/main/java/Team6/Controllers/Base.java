package Team6.Controllers;

import Team6.App;
import Team6.entities.Album;
import Team6.entities.Picture;
import Team6.services.AppLogger;
import Team6.services.PdfHandler;
import javafx.fxml.FXML;
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
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Controller that contains methods that's heavily used.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class Base {

    /**
     * Switches from one given scene to another the next given scene.
     *
     * @param currentScene The current scene
     * @param nextScene The next scene.
     */
    public void switchScene(String currentScene, String nextScene) throws IOException {
        try {
            Context.getInstance().setLastScene(currentScene);
            App.setRoot(nextScene);
        } catch (IOException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    /**
     * Creates a image view for picture with the given filepath.
     * If no image can be found, a
     * {@link FileNotFoundException} will be thrown.
     *
     * @param filePath The filepath.
     * @param elementSize The element size.
     * @return Image view with image.
     */
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

    /**
     * Creates a VBox with padding, in witch the image view will lay in.
     *
     * @param padding The padding of the VBox.
     * @param imagePath The file path.
     * @param elementSize The element size.
     * @return Vbox with image view.
     */
    public VBox createVBox(int padding, String imagePath, int elementSize) throws FileNotFoundException {
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

    /**
     * Returns a list with all the albums icons placed in VBoxes.
     *
     * @param yourAlbums The albums.
     * @param iconPath The album icon path.
     * @return A list with all the Vboxes.
     */
    public List<VBox> createAlbumPages (List<Album> yourAlbums, String iconPath) {
        return yourAlbums.stream().map(x -> {
            VBox vBox = null;
            Text text = new Text(x.getName());
            text.setTextAlignment(TextAlignment.CENTER);
            try {
                text.setWrappingWidth(createImageView(iconPath, 80).getFitWidth());
                vBox = createVBox(10 , iconPath, 80);
            } catch (FileNotFoundException ex) {
                AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
                AppLogger.closeHandler();
            }
            vBox.getChildren().add(text);
            return vBox;
        }).collect(Collectors.toList());
    }

    /**
     * Returns a list with all the pictures placed in VBoxes.
     *If no filepath can be found, a
     * {@link FileNotFoundException} will be thrown.
     *
     * @param pictures A list with pictures.
     * @return A list with all the Vboxes.
     */
    public List<VBox> createPicturePages (List<Picture> pictures) {
        return pictures.stream().map(x -> {
            VBox vBox = null;
            try {
                vBox = createVBox(5 , x.getFilepath(), 170);
            } catch (FileNotFoundException ex) {
                AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
                AppLogger.closeHandler();
            }
            return vBox;
        }).collect(Collectors.toList());
    }

    /**
     * Creates a tile pane witch you can place elements within.
     * @return Tile pane.
     */
    public TilePane elementPane() {
        TilePane tilePane = new TilePane();
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        return tilePane;
    }

    /**
     * Clears a tile pane and add all the Vboxes to it, though creating
     * elements.
     * @param tilePane The tile pane.
     * @param pages The Vbox pages.
     */
    public void createElements(TilePane tilePane, List<VBox> pages) {
        tilePane.getChildren().clear();
        tilePane.getChildren().addAll(pages);
    }

    /**
     * Adds an event to a picture so you can change scene by clicking it.
     *
     * @param pictures List of pictures.
     * @param selectedPictures List of selected picture.
     * @param pages List of Vboxes.
     * @param currentScene The current scene.
     */
    public void setOnMouseClicked(List<Picture> pictures, List<Picture> selectedPictures, List<VBox> pages, String currentScene) {
        pictures.forEach(a -> {
            pages.forEach(v -> {
                if (pictures.indexOf(a) == pages.indexOf(v)) {
                    v.setOnMousePressed(e -> {
                        if (selectedPictures.contains(a)) {
                            v.setStyle("-fx-background-color: transparent");
                            selectedPictures.remove(a);
                        } else {
                            v.setStyle("-fx-background-color: #AAAAAA");
                            selectedPictures.add(a);
                        }
                        if (e.getClickCount() == 2) {
                            Context.getInstance().currentAlbum().setPictures(pictures);
                            Context.getInstance().setIndex(pictures.indexOf(a));
                            try {
                                switchScene(currentScene, "PictureView");
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
     * Select all pictures from the list given, and making them the
     * selected pictures.
     *
     * @param pictures List of all the pictures.
     * @param selectedPictuers List of all the selected pictures.
     * @param pages List of all the VBoxes.
     */
    public void selectAll(List<Picture> pictures, List<Picture> selectedPictuers, List<VBox> pages) {
        pictures.forEach(x -> {
            if (!selectedPictuers.contains(x)) {
                selectedPictuers.add(x);
            }
        });
        pages.forEach(e -> e.setStyle("-fx-background-color: #AAAAAA"));
    }

    /**
     *Creates a input dialog, that shows and wait.
     *
     * @param header The header.
     * @param content The content.
     * @return Input dialog.
     */
    public Optional<String> showInputDialog (String header, String content) {
        TextInputDialog t = new TextInputDialog();
        t.setTitle("PicLd");
        t.setHeaderText(header);
        t.setContentText(content);
        return t.showAndWait();
    }

    /**
     * Creates information dialog, that shows and wait.
     *
     * @param header The header.
     * @param content The content.
     */
    public void showInformationDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Creates PDF.
     */
    @FXML
    void createPdf(List<Picture> selectedPhotos, List<Picture> pictures) {
        PdfHandler pdfHandler = new PdfHandler();
        if (selectedPhotos.isEmpty()) {
            pdfHandler.createPdfAlbum(pictures);
            showInformationDialog("PDF created", "You have created a pdf file with all your album pictures, which is located in download");
        } else {
            pdfHandler.createPdfAlbum(selectedPhotos);
            showInformationDialog("PDF created", "You have created a pdf file with all your selected pictures, which is located in download");
        }
    }
}
