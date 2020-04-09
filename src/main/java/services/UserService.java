package services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entities.Album;
import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserService {
    public UserService() { }

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
            System.out.println(se);
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
        } catch(SQLException se) {
            System.out.println(se);
            return null;
        } finally {
            Database.closeConnection(conn, pst, null);
        }
    }


    public boolean deleteUser(Album album) {
        String query = "Delete from album where id = ?";

        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, album.getId());
            pst.executeUpdate();
            return true;
        } catch(SQLException se) {
            System.out.println(se);
            return false;
        } finally {
            Database.closeConnection(conn, pst);
        }
    }
}
