package services;

import entities.Picture;

import java.util.ArrayList;

public class tagTest {
    public static void main(String[] args) {
        AlbumService a = new AlbumService();
        a.getAllAlbums();
        PictureService p = new PictureService();
        Picture pic = p.getAllPictures(1).get(0);;
        int i = 0;
    }
}
