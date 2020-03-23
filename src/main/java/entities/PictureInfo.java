package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * class used to store information about the photos
 */
public class PictureInfo {
    private InterestingMetadata interestingMetadata;
    private List<String> tags;

    /**
     * cosntructor
     * @param filepath the filepath to the image whose metadata is to be stored
     */
    public PictureInfo(String filepath) {
        this.interestingMetadata = new InterestingMetadata(filepath);
        this.tags = new ArrayList<String>();
    }

    /**
     * method the get the metadata
     * @return returns the relevant metadata
     */
    public InterestingMetadata getInterestingMetadata() {
        return interestingMetadata;
    }

    /**
     *
     * @return returns a list of the photos tags
     */
    public List<String> getTags() {
        return tags;
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
}
