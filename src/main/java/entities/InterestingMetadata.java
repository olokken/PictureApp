package entities;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * Class InterestingMetadata that extracts the wanted metadata from a image.
 * @version unclear 23.03.20
 * @author Team 6
 */

public class InterestingMetadata {
    private String filepath;
    private Date dateTime;
    private int ISO;
    private int shutterSpeed;
    private double exposureTime;
    private boolean isFlashUsed;
    private double latitude;
    private double longitude;
    private double fileSize;

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
    public int getISO() {
        return ISO;
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
     * Gets the shutterspeed.
     * @return int
     */
    public int getShutterSpeed() {
        return shutterSpeed;
    }

    /**
     * Gets the latitude of the image when it was taken.
     * @return double
     */
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getFileSize() {
        return fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    private String fileName;

    public InterestingMetadata(String filepath){
        this.filepath = filepath;
        File file =  new File(filepath);
        this.fileName = file.getName();
        this.fileSize = file.length()/(1024*1024);
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifSubIFDDirectory subIfd = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            GpsDirectory gps = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            if (gps != null) {
                this.latitude = gps.getGeoLocation().getLatitude();
                this.longitude = gps.getGeoLocation().getLongitude();
            }
            if(subIfd != null) {
                this.ISO = subIfd.getInteger(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
                this.exposureTime = subIfd.getDoubleObject(ExifSubIFDDirectory.TAG_EXPOSURE_TIME);
                this.dateTime = subIfd.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                this.shutterSpeed = subIfd.getInteger(ExifSubIFDDirectory.TAG_SHUTTER_SPEED);
            }
            Optional<Tag> flash = subIfd.getTags().stream().filter(x -> x.getTagName().equals("Flash")).findFirst();
            if(flash.isPresent()) {
                if(flash.get().getDescription().equals("Flash fired")) {
                    this.isFlashUsed = true;
                }
                else {
                    this.isFlashUsed = false;
                }
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
