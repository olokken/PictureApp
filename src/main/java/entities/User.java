package entities;

import java.util.ArrayList;

/**
 * class to represent a user
 *
 * @author team6
 */
public class User {
    private String profileName;
    private String password;
    private ArrayList<Album> albums;

    /**
     * constructor for the user class
     * @param profileName name of the user
     * @param password the users password
     */
    public User(String profileName, String password) {
        this.profileName = profileName;
        this.password = password;
        albums = new ArrayList<Album>();
    }
}
