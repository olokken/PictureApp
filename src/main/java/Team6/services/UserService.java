package Team6.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import Team6.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Holds details about the usage of user.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class UserService {

    /**
     * Constructor that creates an instance of UserService, initialising the instance.
     */
    public UserService() { }

    /**
     * Creates a user in the database with the given name and password.
     * If the database won't connect, can't get name or password, or execute,
     * a {@link SQLException} is thrown.
     *
     * @param name The name.
     * @param password The password.
     * @return True if user is created.
     */
    public boolean createUser(String name, String password) {
        String query = "INSERT INTO user VALUES (default, ?,?)";
        Connection conn = Database.connectDB();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, hashPassword(password));
            pst.executeUpdate();
            return true;
        } catch(SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
            return false;
        } finally {
            Database.closeConnection(conn, pst, null);
        }
    }

    /**
     * Hashing the password with adding a salt to the given password.
     *
     * @param password The password.
     * @return A hashed password.
     */
    public String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    /**
     * Returns the user with the given username and password.
     * If the database won't connect, can't get username or password, or execute,
     * a {@link SQLException} is thrown.
     *
     * @param username The username.
     * @param password The password.
     * @return The user.
     */
    public User login (String username, String password) {
        String query = "Select * From user where username = ?";
        Connection conn = Database.connectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, username);
                result = pst.executeQuery();
                if (result.next()) {
                    User user = new User(result.getInt(1), result.getString(2), result.getString(3));
                    System.out.println(user.getUsername());
                    BCrypt.Result res = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                    return res.verified ? user : null;
                }
            return null;
        } catch(SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
            return null;
        } finally {
            Database.closeConnection(conn, pst, null);
        }
    }

    /**
     * Delete user with user ID from the given user.
     * If the database won't connect, can't get user ID or execute,
     * a {@link SQLException} is thrown.
     *
     * @param user The user.
     * @return True if user is deleted.
     */
    public boolean deleteUser(User user) {
        String query = "Delete from user where id = ?";

        Connection conn = Database.connectDB();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, user.getId());
            pst.executeUpdate();
            return true;
        } catch(SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
            return false;
        } finally {
            Database.closeConnection(conn, pst);
        }
    }
}
