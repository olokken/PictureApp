package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlbumRegister {
    private List<Album> albums;
    private Album allPhotos;

    public AlbumRegister() {
        albums = new ArrayList<Album>();
    }

    public void addAlbum(String name) {
        Album a = new Album(name);
        albums.add(a);
    }
    public void removeAlbum(Album a) {
        albums.remove(a);
    }

    public void removeAlbum(String name) {
        albums.remove(findAlbumByName(name));
    }

    private Album findAlbumByName(String albumName) {
        Optional<Album> a = albums.stream().filter(x -> x.getName().equals(albumName)).findFirst();
        if(a.isPresent()){
            return a.get();
        }
        return null;
    }

    public void addPhoto(String albumName, String filePath) {
        findAlbumByName(albumName).addPicture(filePath);
        allPhotos.addPicture(filePath);
    }
}
