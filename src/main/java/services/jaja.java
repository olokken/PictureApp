package services;

import java.io.IOException;

public class jaja {
    public static void main(String[] args) throws IOException {
        AlbumService albumService = new AlbumService();
        albumService.getIdLastAlbumRegistered(1);
        System.out.println(albumService.getIdLastAlbumRegistered(1));
    }
}
