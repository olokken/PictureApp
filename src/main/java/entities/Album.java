package entities;

import idk.AppLogger;
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
     * Constructor that creates an instance of the Album, initialising the instance.
     */
    public Album() {
    }

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
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name The name.
     */
    public void setName(String name) {
        this.name = name;
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
     * Returns all the pictures as a List.
     *
     * @return all the contacts as a List.
     */
    public List<Picture> getPictures() {
        return pictures;
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
     * Checks if a pictures filepath already exists.
     *
     * @param filePath the filepath of the new picture.
     * @return true if the picture already exists.
     */
    private boolean isRegistered(String filePath) {
        Optional<Picture> o = pictures.stream().filter(x -> x.getFilepath().equals(filePath)).findFirst();
        if(o.isPresent()) {
            return true;
        }
        return false;
    }

    /**
     * Add a new picture to the album.
     * If the picture already exists a
     * {@link java.lang.IllegalArgumentException} will be thrown
     * in the try/catch-block.
     *
     * @param filePath The filepath of the picture to be added.
     * @throws IllegalArgumentException if the filepath already exists.
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
     * Remove the picture with the given filepath from the album.
     * The filepath should be one that is currently in use.
     *
     * @param filePath The filepath to the picture to remove.
     */
    public void removePicture(String filePath) {
        Optional<Picture> o = pictures.stream().filter(x -> x.getFilepath().equals(filePath)).findFirst();
        if (o.isPresent()) {
            pictures.remove(o.get());
        }
    }

    /**
     * Sorts the images in the album based on the ISO data by comparing.
     */
    public void sortIso() {
        pictures.sort(Comparator.comparing(x -> x.getIso()));
    }


    /**
     * Sorts the images in the album based on the date by comparing.
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
     * Sorts the images in the album based on the exposure time by comparing.
     */
    public void sortExposureTime() {
        pictures.sort(Comparator.comparing(x -> x.getExposureTime()));
    }

    /**
     * Sorts the images in the album based on the filesize by comparing.
     */
    public void sortFileSize() {
        pictures.sort(Comparator.comparing(x -> x.getFileSize()));
    }

    /**
     * Sorts the pictures alphabetically in the album based on
     * the filename by comparing.
     */
    public void sortFileName() {
        pictures.sort(Comparator.comparing(x -> x.getFileName()));
    }

    /**
     * Sorts the pictures in the album based on if flash is
     * being used, by comparing.
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
