package services;

import entities.Album;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

public class AlbumService {

    //Create logger object from PicLdLogger class.
    //private PicLdLogger picLdLogger = new PicLdLogger();

    public AlbumService() throws IOException { }

    public ArrayList<Album> getAllAlbums(int userId) {
        String query = "Select * from album where userid = ?";
        ArrayList<Album> albums = new ArrayList<>();

        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, userId);
            result = pst.executeQuery();
            while(result.next())
                albums.add(new Album(result.getInt("id"), result.getString("name"), userId));
            return albums;
        } catch(SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
            return null;
        } finally {
            Database.closeConnection(conn, pst, result);
        }
    }

    public boolean createAlbum(String name, int userId) {
        String query = "INSERT INTO album VALUES (default, ?, ?)";
        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.setInt(2, userId);
            pst.executeUpdate();
            return true;
        } catch(SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
            return false;
        } finally {
            Database.closeConnection(conn, pst, null);
        }
    }

    public boolean deleteAlbum(Album album) {
        String query = "Delete from album where id = ?";

        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, album.getId());
            pst.executeUpdate();
            return true;
        } catch(SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
            return false;
        } finally {
            Database.closeConnection(conn, pst);
        }
    }
}
