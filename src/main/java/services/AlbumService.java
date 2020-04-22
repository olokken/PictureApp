package services;

import entities.Album;
import entities.Picture;
import idk.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class AlbumService {

    public AlbumService() { }

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
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
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
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
            return false;
        } finally {
            Database.closeConnection(conn, pst, null);
        }
    }

    public boolean deleteAlbum(Album album) {
        String query = "Delete from album where id = ?";
        String albumPictureQuery = "Select * from albumpicture";
        String deletePictureQuery = "Delete from picture where id = ?";
        List<Picture> pictures = album.getPictures();
        ArrayList<Integer> pictureIds = new ArrayList<>();
        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, album.getId());
            pst.executeUpdate();
            pst = conn.prepareStatement(albumPictureQuery);
            ResultSet result = pst.executeQuery();
            while (result.next()) {
                pictureIds.add(result.getInt(3));
            }
            for (Picture p : pictures) {
                if (!pictureIds.contains(p.getId())) {
                    pst = conn.prepareStatement(deletePictureQuery);
                    pst.setInt(1, p.getId());
                    pst.executeUpdate();
                }
            }
            return true;
        } catch(SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
            return false;
        } finally {
            Database.closeConnection(conn, pst);
        }
    }

    public Integer getIdLastAlbumRegistered(int userId) {
        String query = "SELECT max(id)\n" +
                "FROM album\n" +
                "WHERE userId = ?;";
        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, userId);
            result = pst.executeQuery();
            while (result.next()) {
                return result.getInt("max(id)");
            }
            return null;
        } catch(SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
            return null;
        } finally {
            Database.closeConnection(conn, pst, result);
        }

    }
}
