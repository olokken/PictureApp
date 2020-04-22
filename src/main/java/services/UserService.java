package services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entities.Album;
import entities.User;
import idk.AppLogger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

public class UserService {

    public UserService() throws IOException { }

    public boolean createUser(String name, String password) {
        String query = "INSERT INTO user VALUES (default, ?,?)";
        Connection conn = Database.ConnectDB();
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

    public String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public User login (String username, String password) {
        // result.verified == true
        String query = "Select * From user where username = ?";
        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, username);
                result = pst.executeQuery();
                if (result.next()) {
                    User user = new User(result.getInt(1), result.getString(2), result.getString(3));
                    BCrypt.Result res = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                    return res.verified ? user : null;
                }
            return null;
        } catch(SQLException e) {
            AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
            AppLogger.closeHandler();
            return null;
        } finally {
            Database.closeConnection(conn, pst, null);
        }
    }
    public boolean deleteUser(User user) {
        String query = "Delete from user where id = ?";
        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, user.getId());
            pst.executeUpdate();
            return true;
        } catch(SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
            return false;
        } finally {
            Database.closeConnection(conn, pst);
        }
    }


    /**public boolean deleteUser(User user) {
        String query = "Delete from user where id = ?";
        String deleteAlbumsQuery = "Delete from album where userid = ?";

        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(deleteAlbumsQuery);
            pst.setInt(1, user.getId());
            pst.executeUpdate();
            pst = conn.prepareStatement(query);
            pst.setInt(1, user.getId());
            pst.executeUpdate();
            return true;
        } catch(SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
            return false;
        } finally {
            Database.closeConnection(conn, pst);
        }
    }*/
}
