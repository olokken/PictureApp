/**
 * Class to represent a photoalbum.
 *
 * @author team6
 */

package entities;
//Heja

import idk.AppLogger;
import java.util.*;
import java.util.logging.Level;

public class Album {
    private int id;
    private String name;
    private int userId;
    private List<Picture> pictures;

    /**
     * Constructor for an album object
     * @param name
     */
    public Album(int id, String name, int userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.pictures = new ArrayList<>();
    }

    public Album(String name) {
        this.id = 0;
        this.name = name;
        this.pictures = new ArrayList<>();
    }

    public Album() {}

    public Album(Album a) {
        this.id = a.getId();
        this.name = a.getName();
        this.pictures = a.getPictures();
    }

    public Album(String name, int userId) {
        this.name = name;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        try{
            if(!isRegistered(filePath)) {
                Picture p = new Picture(filePath);
                this.pictures.add(p);
            }
        } catch (IllegalArgumentException e){
            AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
            AppLogger.closeHandler();
        }
    }

    /**
     * Method to remove image from the album
     * @param filePath filepath of the image to be removed
     */
    public void removePicture(String filePath) {
        Optional<Picture> o = pictures.stream().filter(x -> x.getFilepath().equals(filePath)).findFirst();
        if (o.isPresent()) {
            pictures.remove(o.get());
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
     * sorts the images in the album based on the date the photo was taken
     */
    public void sortDate() {
        pictures.sort(Comparator.comparing(x -> {
            if (x.getDateTime() != null) {
                return x.getDateTime().getTime();
            }
            return new Date(0).getTime();
        }));
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
        return name;
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

    public void sortShutterSpeed() {
        pictures.sort(Comparator.comparing(x -> x.getShutterSpeed()));
    }

    public void reverseOrder() {
        Collections.reverse(pictures);
    }
}
