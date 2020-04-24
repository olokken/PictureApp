package Team6.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an user containing albums.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class User {
    private int id;
    private String username;
    private String password;
    private ArrayList<Album> albums;

    /**
     * Sets up the details about the user.
     *
     * @param id The ID.
     * @param username The username.
     * @param password The password.
     */
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        albums = new ArrayList<>();
    }

    /**
     * Constructor that creates an instance of the User, initialising the instance.
     */
    public User() {}

    /**
     * Returns the ID.
     *
     * @return The ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID.
     *
     * @param id The ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the username.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username The username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password The password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns all the albums as a list.
     *
     * @return All the albums as a list.
     */
    public List<Album> getAlbums() {
        return albums;
    }

    /**
     * Sets the albums in a list.
     *
     * @param albums The albums in a list.
     */
    public void setAlbums(List<Album> albums) {
        this.albums = (ArrayList<Album>) albums;
    }

}
