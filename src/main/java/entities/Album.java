/**
 * Class to represent a photoalbum
 *
 * @author team6
 */
package entities;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Album {
    private int id;
    private String name;
    private List<Picture> pictures;

    /**
     * Constructor for an album object
     * @param name
     */
    public Album(int id, String name) {
        this.id = id;
        this.name = name;
        this.pictures = new ArrayList<Picture>();
    }

    public Album(String name) {
        this.id = 0;
        this.name = name;
        this.pictures = new ArrayList<Picture>();
    }

    public Album() {}

    public Album(Album a) {
        this.id = a.getId();
        this.name = a.getName();
        this.pictures = a.getPictures();
    }

    public void setPictures(ArrayList<Picture> pictures) {
        this.pictures = pictures;
    }

    public int getId() {
        return id;
    }

    /**
     * method to get the name of the album
     * @return the name of the album
     */
    public String getName() {
        return name;
    }

    /**
     * method to get picures in the album
     * @return returns a list of the pictures in the album
     */
    public List<Picture> getPictures() {
        return pictures;
    }

    /**
     * Method to check if a photo is registered in the album
     * @param filePath the filepath to the picture that is being checked
     * @return true if registered, false if not
     */
    private boolean isRegistered(String filePath) {
        Optional<Picture> o = pictures.stream().filter(x -> x.getFilepath().equals(filePath)).findFirst();
        if(o.isPresent()) {
            return true;
        }
        return false;
    }

    /**
     * Method to add a picture to the album, only if it isnt already
     * @param filePath filepath of the image to be added
     */
    public void addPicture(String filePath) {
        if(isRegistered(filePath)) {
            throw new IllegalArgumentException("Bildet er allerede registert");
        }
        Picture p = new Picture(filePath);
        this.pictures.add(p);
    }

    /**
     * Method to remove image from the album
     * @param filePath filepath of the image to be removed
     */
    public void removePicture(String filePath) {
        Optional<Picture> o = pictures.stream().filter(x -> x.getFilepath().equals(filePath)).findFirst();
        if (o.isPresent()) {
            pictures.remove(o);
        }
    }

    /**
     * method to remove a picture
     * @param p picture to be removed
     */
    public void removePicture(Picture p) {
        pictures.remove(p);
    }

    /**
     * sorts the images in the album based on the ISO data
     */
    public void sortIso() {
        pictures.sort(Comparator.comparing(x -> x.getISO()));
    }

    /**
     * sorts the images in the album based on the ISO data in the opposite order
     */
    public void sortIsoReversed() {
        pictures.sort(Comparator.comparing(x -> x.getISO()));
    }

    /**
     * sorts the images in the album based on the date the photo was taken
     */
    public void sortDate() {
        pictures.sort(Comparator.comparing(x -> x.getDateTime()));
    }

    /**
     * sorts the images in the album based on the exposure time
     */
    public void sortExposureTime() {
        pictures.sort(Comparator.comparing(x -> x.getExposureTime()));
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    /**
     * sorts the images in the album based on the file size
     */
    public void sortFileSize() {
        pictures.sort(Comparator.comparing(x -> x.getFileSize()));
    }

    @Override
    public String toString() {
        return name + "  Inneholder : " + pictures.size() + " bilder";
    }

    /**
     * sorts the images in the album based on file name
     */
    public void sortFileName() {
        pictures.sort(Comparator.comparing(x -> x.getFileName()));
    }

    /**
     * sorts the images in the album based on whether flash was used
     */
    public void sortFlashUsed() {
        pictures.sort(Comparator.comparing(x -> x.isFlashUsed()));
    }
}
