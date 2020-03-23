package forkastet;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class forkastet {
    private String filepath;
    private LocalDateTime dateTime;
    private int ISO;
    private String GPSLatitude;
    private String GPSLongitude;
    private int fileSize;
    private String fileName;

    public String getFilepath() {
        return filepath;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getISO() {
        return ISO;
    }

    public String getGPSLatitude() {
        return GPSLatitude;
    }

    public String getGPSLongitude() {
        return GPSLongitude;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public forkastet(String filepath) {
        this.filepath = filepath;
        File file = new File(filepath);
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            List<Directory> list = (List<Directory>) metadata.getDirectories();
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if (tag.getTagName().equals("Date/Time")) {
                        String[] tid = tag.getDescription().split("[ :]");
                        this.dateTime = LocalDateTime.of(Integer.parseInt(tid[0]), Integer.parseInt(tid[1]), Integer.parseInt(tid[2]),
                                Integer.parseInt(tid[3]), Integer.parseInt(tid[4]), Integer.parseInt(tid[5]));
                    }
                        if(tag.getTagName().equals("File Modified Date")) {
                            //dateTime = file.lastModified();
                        }
                            if(tag.getTagName().equals("ISO Speed Ratings")) {
                                this.ISO = Integer.parseInt(tag.getDescription());
                            }
                                if(tag.getTagName().equals("GPS Latitude")) {
                                    this.GPSLatitude = tag.getDescription();
                                }
                                    if(tag.getTagName().equals("GPS Longitude")) {
                                        this.GPSLongitude = tag.getDescription();
                                    }
                                    if(tag.getTagName().equals("File Size")) {
                                        this.fileSize = Integer.parseInt(tag.getDescription().substring(0, tag.getDescription().indexOf(' ')));
                                    }
                                        if(tag.getTagName().equals("File Name")) {
                                            this.fileName = tag.getDescription();
                                        }

                }

                if (directory.hasErrors()) {
                    for (String error : directory.getErrors()) {
                        System.err.println("ERROR: " + error);
                    }
                }
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
