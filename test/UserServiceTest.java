import ch.qos.logback.core.db.DBHelper;
import Team6.entities.User;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import Team6.services.UserService;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Testclass testing UserService
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {
    private static UserService userService;
    private static String username;
    private static String password;

    @BeforeAll
    static void beforeAll() throws IOException {
        userService = new UserService();
        username = "Berit";
        password = "password123";
    }

    @Test
    @Order(1)
    public void testCreateUser(){
        assertTrue(userService.createUser(username, password));
    }

    @Test
    @Order(2)
    public void testHashPassword(){
        assertNotEquals(userService.hashPassword(password), password);
    }

    @Test
    @Order(3)
    public void testDeleteUser(){
        User user = userService.login("Berit", "password123");
        assertTrue(userService.deleteUser(user));
    }
}

