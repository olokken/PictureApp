package entities;

import java.util.ArrayList;

/**
 * class to represent a user
 *
 * @author team6
 */
public class User {
    private int id;
    private String username;
    private String password;
    private ArrayList<Album> albums;

    /**
     * constructor for the user class
     * @param username name of the user
     * @param password the users password
     */
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        albums = new ArrayList<Album>();
    }

    public User() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

}
