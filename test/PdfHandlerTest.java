import Team6.Context;
import entities.Album;
import entities.Picture;
import entities.User;
import org.junit.jupiter.api.*;
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
    private static PdfHandler pdfHandler;
    private static User user;
    private static UserService userService;
    private static Album album;
    private static AlbumService albumService;
    private static Picture picture;
    private static PictureService pictureService;
    private static ArrayList<Picture> pictures;

    @BeforeAll
    static void beforeAll() throws IOException {
        pdfHandler = new PdfHandler();
        userService = new UserService();
        userService.createUser("Klara", "password123");
        user = userService.login("Klara", "password123");
        albumService = new AlbumService();
        albumService.createAlbum("Test Album", user.getId());
        pictureService = new PictureService();
        picture = new Picture("./test/garn.jpg");
        album = albumService.getAllAlbums(user.getId()).get(0);
        pictures = new ArrayList<Picture>();

        pictureService.createPicture(picture, album.getId());

    }

    @Test
    @Order(1)
    public void testCreatePdfDocument() throws FileNotFoundException {
        assertNotNull(pdfHandler.createPfdDocument());
    }
    @Test
    @Order(2)
    public void testCreatePdfAlbum() throws FileNotFoundException {
        pictures.add(pictureService.getAllPictures(album.getId(), user.getId()).get(0));
        assertTrue(pdfHandler.createPdfAlbum(pictures));
    }

    @AfterAll
    static void afterAll(){
        pictureService.deletePicture(pictureService.getAllPictures(album.getId(), user.getId()).get(0).getId(), album.getId());
        albumService.deleteAlbum(album);
        userService.deleteUser(user);
    }
}
