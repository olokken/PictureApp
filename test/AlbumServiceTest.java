import entities.Album;
import entities.User;
import org.junit.jupiter.api.*;
import services.AlbumService;
import services.UserService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlbumServiceTest {
    private static AlbumService albumService;
    private static UserService userService;
    private static User user;

    @BeforeAll
    static void beforeAll() throws IOException {
        albumService = new AlbumService();
        userService = new UserService();
        userService.createUser("Anne", "password123");
        user = userService.login("Anne", "password123");
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
    @AfterAll
    static void testDeleteUser(){
        userService.deleteUser(user);
    }
}

//hh
