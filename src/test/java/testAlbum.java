import entities.Album;

public class testAlbum {
    public static void main(String[] args) {
        Album a = new Album("Kulegutta");
        a.addPicture("C:/Users/olelo/Downloads/ublitz.jpg");
        a.addPicture("C:/Users/olelo/OneDrive/Bilder/Kamerabilder/WIN_20200308_21_00_11_Pro.jpg");
        a.addPicture("C:/Users/olelo/Downloads/blitz.jpg");
        a.sortIso();
        a.sortFlashUsed();
        int i = 0;
    }
}
