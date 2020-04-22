import Team6.Context;
import entities.Album;
import entities.Picture;
import entities.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import services.AlbumService;
import services.PdfHandler;
import services.PictureService;
import services.UserService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PdfHandlerTest {
    private PdfHandler pdfHandler;
    private User user;
    private UserService userService;
    private Album album;
    private AlbumService albumService;
    private Picture picture;
    private PictureService pictureService;
    private ArrayList<Picture> pictures;

    public PdfHandlerTest() throws IOException {
        this.pdfHandler = new PdfHandler();
        this.userService = new UserService();
        userService.createUser("Klara", "password123");
        this.user = userService.login("Klara", "password123");
        this.albumService = new AlbumService();
        albumService.createAlbum("Test Album", user.getId());
        this.pictureService = new PictureService();
        this.picture = new Picture("./test/garn.jpg");
        this.album = albumService.getAllAlbums(user.getId()).get(0);
        this.pictures = new ArrayList<Picture>();
    }

    @Test
    @Order(1)
    public void testCreatePicture(){
        assertTrue(pictureService.createPicture(picture, album.getId()));
    }

    @Test
    @Order(2)
    public void testCreatePdfDocument() throws FileNotFoundException {
        assertNotNull(pdfHandler.createPfdDocument());
    }
    @Test
    @Order(3)
    public void testCreatePdfAlbum() throws FileNotFoundException {
        pictures.add(pictureService.getAllPictures(album.getId(), user.getId()).get(0));
        assertTrue(pdfHandler.createPdfAlbum(pictures));
    }
    @Test
    @Order(4)
    public void testDeletePicture(){
        assertTrue(pictureService.deletePicture(pictureService.getAllPictures(album.getId(), user.getId()).get(0).getId(), album.getId()));
    }

    @Test
    @Order(5)
    public void testDeleteAlbum(){
        Album album = albumService.getAllAlbums(user.getId()).get(0);
        assertTrue(albumService.deleteAlbum(album));
    }
    @Test
    @Order(6)
    public void testDeleteUser() {
        userService.deleteUser(user);
    }
}
