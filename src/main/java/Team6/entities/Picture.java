package Team6.entities;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import Team6.services.AppLogger;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

/**
 * Holds details about a picture, like filepath, iso and shutter speed.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class Picture {
    private int id;
    private String filepath;
    private Date dateTime;
    private int iso;
    private int shutterSpeed;
    private double exposureTime;
    private boolean isFlashUsed;
    private double latitude;
    private double longitude;
    private double fileSize;
    private String fileName;
    private List<String> tags;


    /**
     * Basic constructor being used when retrieving picture details
     * from database.
     *
     * @param id The ID.
     * @param fileName The filename.
     * @param filepath The filepath.
     * @param fileSize The file size.
     * @param dateTime The date and time.
     * @param iso The iso.
     * @param shutterSpeed The shutter speed.
     * @param exposureTime The exposure time.
     * @param isFlashUsed If the flashed is used.
     * @param latitude The latitude.
     * @param longitude The longitude.
     */
    public Picture(int id, String fileName, String filepath, double fileSize, Date dateTime, int iso, int shutterSpeed, double exposureTime, boolean isFlashUsed, double latitude, double longitude) {
        this.id = id;
        this.filepath = filepath;
        this.dateTime = dateTime;
        this.iso = iso;
        this.shutterSpeed = shutterSpeed;
        this.exposureTime = exposureTime;
        this.isFlashUsed = isFlashUsed;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.tags = new ArrayList<>();
    }

    /**
     * Sets up the metadata information.
     * If the picture cant be processed, or if metadata
     * can't be read, a {@link ImageProcessingException} or
     * {@link IOException} is thrown in the try/catch-block.
     *
     * @param filepath The filepath to the image whose
     *                 metadata is to be stored.
     */
    public Picture(String filepath) {
        this.filepath = filepath;
        File file =  new File(filepath);
        this.fileName = file.getName();
        this.fileSize = file.length()/1024;
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifSubIFDDirectory subIfd = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            GpsDirectory gps = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            if (gps != null) {
                this.latitude = gps.getGeoLocation().getLatitude();
                this.longitude = gps.getGeoLocation().getLongitude();
            }
            if(subIfd != null) {
                this.iso = subIfd.getInteger(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
                this.exposureTime = subIfd.getDoubleObject(ExifSubIFDDirectory.TAG_EXPOSURE_TIME);
                this.dateTime = subIfd.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                this.shutterSpeed = subIfd.getInteger(ExifSubIFDDirectory.TAG_SHUTTER_SPEED);
                Optional<Tag> flash = subIfd.getTags().stream().filter(x -> x.getTagName().equals("Flash")).findFirst();
                if(flash.isPresent()) {
                    if(flash.get().getDescription().equals("Flash fired")) {
                        this.isFlashUsed = true;
                    }
                    else {
                        this.isFlashUsed = false;
                    }
                }
            }
        } catch (ImageProcessingException | IOException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
        this.tags = new ArrayList<>();
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
     * Returns the filepath.
     *
     * @return The filepath.
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * Sets the filepath.
     *
     * @param filepath The filepath.
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Returns the date and time the picture is taken.
     *
     * @return The date and time.
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * Sets the date and time.
     *
     * @param dateTime The date and time.
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Returns the ISO.
     *
     * @return The ISO.
     */
    public int getIso() {
        return iso;
    }

    /**
     * Sets the ISO.
     *
     * @param iso The ISO.
     */
    public void setIso(int iso) {
        this.iso = iso;
    }

    /**
     * Returns the shutter speed.
     *
     * @return The shutter speed.
     */
    public int getShutterSpeed() {
        return shutterSpeed;
    }

    /**
     * Sets the shutter speed.
     *
     * @param shutterSpeed The shutter speed.
     */
    public void setShutterSpeed(int shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
    }

    /**
     * Returns the exposure time.
     *
     * @return The exposure time.
     */
    public double getExposureTime() {
        return exposureTime;
    }

    /**
     * Sets the exposure time.
     *
     * @param exposureTime The exposure time.
     */
    public void setExposureTime(double exposureTime) {
        this.exposureTime = exposureTime;
    }

    /**
     * If the flash is used in the picture.
     *
     * @return True if the flashed is being used.
     */
    public boolean isFlashUsed() {
        return isFlashUsed;
    }

    /**
     * Sets the if the flash is being used or not.
     *
     * @param flashUsed True if the flash is being used.
     */
    public void setFlashUsed(boolean flashUsed) {
        isFlashUsed = flashUsed;
    }

    /**
     * Returns the latitude.
     *
     * @return The latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude.
     *
     * @param latitude The latitude.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the longitude.
     *
     * @return The longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude.
     *
     * @param longitude The longitude.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the file size.
     *
     * @return The file size.
     */
    public double getFileSize() {
        return fileSize;
    }

    /**
     * Sets the file size.
     *
     * @param fileSize The file size.
     */
    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * Returns the filename.
     *
     * @return The filename.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the filename.
     *
     * @param fileName The filename.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns all the tags as a list.
     *
     * @return All the tags as a list.
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Sets the tags in a list.
     *
     * @param tags The tags in a list.
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Test for content equality between two objects.
     *
     * @param o The object to compare to this one.
     * @return Filepath if the argument object is instance
     *         of Picture.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Picture)) return false;
        Picture picture = (Picture) o;
        return filepath.equals(picture.filepath);
    }

    /**
     * adds a tag to the image
     * @param tag the tag to be added
     * @return returns true
     */
    public boolean addTags(String tag) {
        tags.add(tag);
        return true;
    }

    /**
     * Returns a string with the filename of the picture..
     *
     * @return A string containing the filename.
     */
    @Override
    public String toString() {
        return fileName;
    }

    /**
     * Compute a hashcode.
     *
     * @return A hashcode for picture.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, filepath, dateTime, iso, shutterSpeed, exposureTime, isFlashUsed, latitude, longitude, fileSize, fileName, tags);
    }
}
