package entities;

import java.util.ArrayList;

public class User {
    private String profileName;
    private String password;
    private ArrayList<Album> albums;

    public User(String profileName, String password) {
        this.profileName = profileName;
        this.password = password;
        albums = new ArrayList<Album>();
    }
}
