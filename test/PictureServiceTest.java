import entities.Album;
import entities.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import services.AlbumService;
import services.PictureService;
import entities.Picture;
import services.UserService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PictureServiceTest {
    private User user;
    private UserService userService;
    private Album album;
    private AlbumService albumService;
    private Picture picture;
    private PictureService pictureService;

    public PictureServiceTest() throws IOException {
        this.userService = new UserService();
        userService.createUser("Lilli", "password123");
        this.user = userService.login("Lilli", "password123");
        this.albumService = new AlbumService();
        albumService.createAlbum("Test Album", user.getId());
        this.pictureService = new PictureService();
        this.picture = new Picture("./test/garn.jpg");
        this.album = albumService.getAllAlbums(user.getId()).get(0);
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

    @Test
    @Order(5)
    public void testDeleteAlbum(){
        Album album = albumService.getAllAlbums(user.getId()).get(0);
        assertTrue(albumService.deleteAlbum(album));
    }
    @Test
    @Order(6)
    public void testDeleteUser(){
        userService.deleteUser(user);
    }
}
