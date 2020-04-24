package Team6.entities;

import Team6.services.AppLogger;
import java.util.*;
import java.util.logging.Level;
/**
 * Represents an photo Album containing pictures with picture information.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class Album {
    private int id;
    private String name;
    private int userId;
    private List<Picture> pictures;

    /**
     * Sets up the details about the album.
     *
     * @param name The name.
     * @param id The ID.
     * @param userId The ID of the user.
     */
    public Album(int id, String name, int userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.pictures = new ArrayList<>();
    }

    /**
     * Sets up the details about the album.
     *
     * @param name The name.
     */
    public Album(String name) {
        this.id = 0;
        this.name = name;
        this.pictures = new ArrayList<>();
    }

    /**
     * Constructor that creates an instance of the Album, initialising the instance.
     */
    public Album() {}

    /**
     * Sets up the details about the album.
     *
     * @param album an Album.
     */
    public Album(Album album) {
        this.id = album.getId();
        this.name = album.getName();
        this.pictures = album.getPictures();
    }

    /**
     * Sets up the details about the album.
     *
     * @param name The name.
     * @param userId The ID of the user.
     */
    public Album(String name, int userId) {
        this.name = name;
        this.userId = userId;
    }


    /**
     * Returns the ID.
     *
     * @return The ID.
     */
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
     * Returns the user ID.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId The user ID.
     */
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
        pictures.sort(Comparator.comparing(x -> x.getIso()));
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

    /**
     * Sets the ID.
     *
     * @param id The ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name.
     *
     * @return the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the pictures.
     *
     * @param pictures The list with pictures.
     */
    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    /**
     * sorts the images in the album based on the file size
     */
    public void sortFileSize() {
        pictures.sort(Comparator.comparing(x -> x.getFileSize()));
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

    /**
     * Sorts the pictures in the album based on the shutter speed,
     * by comparing.
     */
    public void sortShutterSpeed() {
        pictures.sort(Comparator.comparing(x -> x.getShutterSpeed()));
    }

    /**
     * Reverses the order of the pictures.
     */
    public void reverseOrder() {
        Collections.reverse(pictures);
    }

    /**
     * Returns a string with the name of the album.
     *
     * @return A string containing the album name.
     */
    @Override
    public String toString() {
        return name;
    }
}
