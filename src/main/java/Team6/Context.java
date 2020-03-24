package Team6;

import entities.Album;

public class Context {
    private final static Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }

    private Album album = new Album();

    public Album currentAlbum() {
        return album;
    }
}
