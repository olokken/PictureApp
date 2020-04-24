import Team6.entities.Album;
import Team6.entities.User;
import org.junit.jupiter.api.*;
import Team6.services.AlbumService;
import Team6.services.PictureService;
import Team6.entities.Picture;
import Team6.services.UserService;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PictureServiceTest {
    private static User user;
    private static UserService userService;
    private static Album album;
    private static AlbumService albumService;
    private static Picture picture;
    private static PictureService pictureService;

    @BeforeAll
    static void beforeAll() throws IOException {
        userService = new UserService();
        userService.createUser("Lilli", "password123");
        user = userService.login("Lilli", "password123");
        albumService = new AlbumService();
        albumService.createAlbum("Test Album", user.getId());
        pictureService = new PictureService();
        picture = new Picture("./Team6.Controllers.test/garn.jpg");
        album = albumService.getAllAlbums(user.getId()).get(0);
    }

    @Test
    @Order(1)
    public void testCreatePicture(){
        assertTrue(pictureService.createPicture(picture, album.getId()));
    }
    @Test
    @Order(2)
    public void testAddTag(){
        assertTrue(pictureService.addTag(pictureService.getAllPictures(album.getId(), user.getId()).get(0), "Tag"));
    }

    @Test
    @Order(3)
    public void testDeleteTag(){
        assertTrue(pictureService.deleteTag(pictureService.getAllPictures(album.getId(), user.getId()).get(0), "Tag"));
    }

    @Test
    @Order(4)
    public void testDeletePicture(){
        assertTrue(pictureService.deletePicture(pictureService.getAllPictures(album.getId(), user.getId()).get(0).getId(), album.getId()));
    }

    @AfterAll
    static void afterAll(){
        albumService.deleteAlbum(album);
        userService.deleteUser(user);
    }
}
