package services;

import entities.Album;

import java.util.ArrayList;

public class AlbumService {
    public AlbumService() {

    }

    public ArrayList<Album> getAllAlbums() {
        return new ArrayList<>() {
            {
                add(new Album("Vinter"));
                add(new Album("Sommer"));
                add(new Album("Vår"));
                add(new Album("Høst"));
            }
        };
    }
}
