import entities.Album;
import entities.User;
import org.junit.AfterClass;
import org.junit.jupiter.api.*;
import services.AlbumService;
import services.UserService;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlbumServiceTest {
    private AlbumService albumService;
    private UserService userService;
    private User user;

    @BeforeEach
    public void beforeEach() throws IOException {
        this.albumService = new AlbumService();
        this.userService = new UserService();
        this.userService.createUser("Anne", "password123");
        this.user = userService.login("Anne", "password123");
    }

    @Test
    @Order(1)
    public void testCreateAlbum(){
        assertTrue(albumService.createAlbum("TestAlbum", user.getId()));
    }

    @Test
    @Order(2)
    public void testDeleteAlbum(){
        Album album = albumService.getAllAlbums(user.getId()).get(0);
        assertTrue(albumService.deleteAlbum(album));
    }
    @Test
    @Order(3)
    public void testDeleteUser(){
        userService.deleteUser(user);
    }
}
