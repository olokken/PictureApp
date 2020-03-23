package forkastet;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;

public class Gauder {
    public static void main(String[] args) {
        File file = new File("C:/Users/olelo/OneDrive/Bilder/Kamerabilder/WIN_20200308_21_00_11_Pro.jpg");
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifSubIFDDirectory subIfd = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.println(tag);
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
