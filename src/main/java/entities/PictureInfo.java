package entities;

import java.util.ArrayList;
import java.util.List;

public class PictureInfo {
    private InterestingMetadata interestingMetadata;
    private List<String> tags;

    public PictureInfo(String filepath) {
        this.interestingMetadata = new InterestingMetadata(filepath);
        this.tags = new ArrayList<String>();
    }

    public InterestingMetadata getInterestingMetadata() {
        return interestingMetadata;
    }

    public List<String> getTags() {
        return tags;
    }

    public boolean addTags(String tag) {
        tags.add(tag);
        return true;
    }
}
