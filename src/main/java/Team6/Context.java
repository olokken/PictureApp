package Team6;

import entities.Album;
import entities.Picture;

import java.util.ArrayList;
import java.util.List;

public class Context {
    private final static Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }
    private int index = 0;
    private List<Picture> pictures;
    private Album album = new Album();
    private ArrayList<Album> albums;


    public ArrayList<Album> currentAlbums() {
        return albums;
    }

    public Album currentAlbum() {
        return album;
    }

    public List<Picture> currentPictures() {
        return pictures;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int currentIndex () {
        return index;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }
}
