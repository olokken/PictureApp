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
 * class used to store information about the photos
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

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

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
     * constructor
     * @param filepath the filepath to the image whose metadata is to be stored
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

    public int getId() {
        return id;
    }

    /**
     *
     * @return returns a list of the photos tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Gets the filepath for the image.
     * @return String
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * Gets the time the picture was taken.
     * @return Date
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * Gets ISO.
     * @return int
     */
    public int getIso() {
        return iso;
    }

    /**
     * Gets the shutterspeed.
     * @return int
     */
    public int getShutterSpeed() {
        return shutterSpeed;
    }

    /**
     * Get the exposure time.
     * @return double
     */
    public double getExposureTime() {
        return exposureTime;
    }

    /**
     * If the flash was used the method returns true.
     * @return boolean
     */
    public boolean isFlashUsed() {
        return isFlashUsed;
    }

    /**
     * Gets the latitude of the image when it was taken.
     * @return double
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude of the image when it was taken.
     * @return double
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Gets file size
     * @return double
     */
    public double getFileSize() {
        return fileSize;
    }

    /**
     * Gets filename
     * @return String
     */
    public String getFileName() {
        return fileName;
    }

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

    @Override
    public String toString() {
        return fileName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filepath, dateTime, iso, shutterSpeed, exposureTime, isFlashUsed, latitude, longitude, fileSize, fileName, tags);
    }
}