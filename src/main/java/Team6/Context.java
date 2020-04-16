package Team6;

import entities.Album;
import entities.Picture;
import entities.User;

import java.util.ArrayList;
import java.util.List;

public class Context {
    private final static Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }
    private String searchingWord;
    private int index;
    private Album album = new Album();
    private User user = new User();
    private ArrayList<Album> albums;
    private boolean switchToMap;

    public boolean currentSwitchToMap () {
        return switchToMap;
    }
    public void setSwitchToMap(boolean b) {
        switchToMap = b;
    }

    public User currentUser() {
        return user;
    }
    public ArrayList<Album> currentAlbums() {
        return albums;
    }

    public Album currentAlbum() {
        return album;
    }

    public String currentSearchingword() {
        return searchingWord;
    }

    public void setCurrentSearchingword(String searchingWord) {
        this.searchingWord = searchingWord;
    }


    public void setIndex(int index) {
        this.index = index;
    }

    public int currentIndex () {
        return index;
    }

}
