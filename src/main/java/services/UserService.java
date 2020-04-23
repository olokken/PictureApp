package services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entities.User;
import idk.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class UserService {

    public UserService() { }

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

    public String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

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
