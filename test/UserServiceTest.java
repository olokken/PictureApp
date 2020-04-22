import ch.qos.logback.core.db.DBHelper;
import entities.User;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Testclass testing UserService
 */
public class UserServiceTest {
    private UserService userService;
    private String username;
    private String password;

    @BeforeEach
    public void beforeEach() throws IOException {
        this.userService = new UserService();
        this.username = "Berit";
        this.password = "password123";
    }

    @Test
    public void testCreateUser(){
        assertTrue(userService.createUser(username, password));
    }

    @Test
    public void testHashPassword(){
        assertNotEquals(userService.hashPassword(password), this.password);
    }

    @Test
    public void testDeleteUser(){
        User user = userService.login("Berit", "password123");
        assertTrue(userService.deleteUser(user));
    }
}

