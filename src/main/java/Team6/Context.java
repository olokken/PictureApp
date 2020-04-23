package Team6;

import entities.Album;
import entities.User;


public class Context {
    private final static Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }
    private String searchingWord;
    private int index;
    private Album album = new Album();
    private User user = new User();
    private String lastScene;

    public String getLastScene () {
        return lastScene;
    }

    public void setLastScene(String scene) {
        lastScene = scene;
    }

    public User currentUser() {
        return user;
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
