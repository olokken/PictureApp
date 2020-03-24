package services;

import entities.Album;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AlbumService {
    public AlbumService() { }

    public ArrayList<Album> getAllAlbums() {
        String query = "Select * from album";
        ArrayList<Album> albums = new ArrayList<>();

        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(query);
            result = pst.executeQuery();

            while(result.next())
                albums.add(new Album(result.getInt("id"), result.getString("name")));
            return albums;
        } catch(SQLException se) {
            System.out.println(se);
            return null;
        } finally {
            Database.closeConnection(conn, pst, result);
        }
    }

    public boolean createAlbum(String name) {
        String query = "INSERT INTO album VALUES (default, ?)";

        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.executeUpdate();
            return true;
        } catch(SQLException se) {
            System.out.println(se);
            return false;
        } finally {
            Database.closeConnection(conn, pst, result);
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
            System.out.println(se);
            return false;
        } finally {
            Database.closeConnection(conn, pst);
        }
    }


}
