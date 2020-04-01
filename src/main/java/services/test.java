package services;

import entities.Picture;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        PictureService p = new PictureService();
        Picture pic = new Picture("C:/Users/olelo/OneDrive/Bilder/Domenemodell.PNG");
        int i = 0;
        p.createPicture(pic,14);
    }
}
