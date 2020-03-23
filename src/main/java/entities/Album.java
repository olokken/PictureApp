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
    private String name;
    private List<PictureInfo> pictures;

    /**
     * Constructor for an album object
     * @param name
     */
    public Album(String name) {
        this.name = name;
        this.pictures = new ArrayList<PictureInfo>();
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
    public List<PictureInfo> getPictures() {
        return pictures;
    }

    /**
     * Method to check if a photo is registered in the album
     * @param filePath the filepath to the picture that is being checked
     * @return true if registered, false if not
     */
    private boolean isRegistered(String filePath) {
        Optional<PictureInfo> o = pictures.stream().filter(x -> x.getInterestingMetadata().getFilepath().equals(filePath)).findFirst();
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
        PictureInfo p = new PictureInfo(filePath);
        this.pictures.add(p);
    }

    /**
     * Method to remove image from the album
     * @param filePath filepath of the image to be removed
     */
    public void removePicture(String filePath) {
        Optional<PictureInfo> o = pictures.stream().filter(x -> x.getInterestingMetadata().getFilepath().equals(filePath)).findFirst();
        if (o.isPresent()) {
            pictures.remove(o);
        }
    }

    /**
     * method to remove a picture
     * @param p picture to be removed
     */
    public void removePicture(PictureInfo p) {
        pictures.remove(p);
    }

    /**
     * sorts the images in the album based on the ISO data
     */
    public void sortIso() {
        pictures.sort(Comparator.comparing(x -> x.getInterestingMetadata().getISO()));
    }

    /**
     * sorts the images in the album based on the ISO data in the opposite order
     */
    public void sortIsoReversed() {
        pictures.sort(Comparator.comparing(x -> x.getInterestingMetadata().getISO()));
    }

    /**
     * sorts the images in the album based on the date the photo was taken
     */
    public void sortDate() {
        pictures.sort(Comparator.comparing(x -> x.getInterestingMetadata().getDateTime()));
    }

    /**
     * sorts the images in the album based on the exposure time
     */
    public void sortExposureTime() {
        pictures.sort(Comparator.comparing(x -> x.getInterestingMetadata().getExposureTime()));
    }

    /**
     * sorts the images in the album based on the file size
     */
    public void sortFileSize() {
        pictures.sort(Comparator.comparing(x -> x.getInterestingMetadata().getFileSize()));
    }

    @Override
    public String toString() {
        return name + "  Inneholder : " + pictures.size() + " bilder";
    }

    /**
     * sorts the images in the album based on file name
     */
    public void sortFileName() {
        pictures.sort(Comparator.comparing(x -> x.getInterestingMetadata().getFileName()));
    }

    /**
     * sorts the images in the album based on whether flash was used
     */
    public void sortFlashUsed() {
        pictures.sort(Comparator.comparing(x -> x.getInterestingMetadata().isFlashUsed()));
    }
}
