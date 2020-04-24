package Team6;

import entities.Album;
import entities.User;

/**
 * Holds information between controllers.
 *
 * @author Team 6
 * @version 2020.04.24
 */
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

    /**
     * Returns the last scene.
     *
     * @return The last scene.
     */
    public String getLastScene () {
        return lastScene;
    }

    /**
     * Sets the last scene.
     *
     * @param scene The last scene.
     */
    public void setLastScene(String scene) {
        lastScene = scene;
    }

    /**
     * Returns current user.
     *
     * @return The current user.
     */
    public User currentUser() {
        return user;
    }

    /**
     * Returns current album.
     *
     * @return The current album.
     */
    public Album currentAlbum() {
        return album;
    }

    /**
     * Returns the current search word.
     *
     * @return The current search word.
     */
    public String currentSearchingword() {
        return searchingWord;
    }

    /**
     * Sets the current search word.
     *
     * @param searchingWord The current search word.
     */
    public void setCurrentSearchingword(String searchingWord) {
        this.searchingWord = searchingWord;
    }

    /**
     * Returns the current index.
     *
     * @return The current index.
     */
    public int currentIndex () {
        return index;
    }
    /**
     * Sets the current index.
     *
     * @param index The current index.
     */
    public void setIndex(int index) {
        this.index = index;
    }

}
