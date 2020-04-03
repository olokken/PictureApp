package Team6;

import entities.Album;
import entities.Picture;

import java.util.List;

public class Context {
    private final static Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }
    private int index = 0;
    private List<Picture> pictures;
    private Album album = new Album();

    public Album currentAlbum() {
        return album;
    }

    public List<Picture> currentPictures() {
        return pictures;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public int currentIndex () {
        return index;
    }
}
