package services;

import entities.Picture;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        PictureService p = new PictureService();
        ArrayList<Picture> pic = p.getAllPictures(1);
        for (Picture pe : pic) {
            p.deletePicture(pe);
        }
    }
}
