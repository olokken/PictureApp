package services;

import entities.Album;
import entities.Picture;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PictureService {
    public PictureService() { }

    public ArrayList<Picture> getAllPictures(int albumId) {
        String query = "SELECT * From picture as p INNER JOIN albumpicture as ap WHERE ap.albumId = ?";
        ArrayList<Picture> pictures = new ArrayList<>();

        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1,albumId);
            result = pst.executeQuery();

            while(result.next())
                pictures.add(new Picture(result.getInt("id"), result.getString("fileName"), result.getString("filePath"),
                        result.getDouble("fileSize"), result.getDate("dateTaken"), result.getInt("iso"), result.getInt("shutterSpeed"), result.getDouble("exposureTime"),
                        result.getBoolean("isFlashUsed"), result.getDouble("latitude"), result.getDouble("longitude")));
            return pictures;
        } catch(SQLException se) {
            System.out.println(se);
            return null;
        } finally {
            Database.closeConnection(conn, pst, result);
        }
    }

    public boolean createPicture(Picture picture, int albumId) {
        String insertPicture = "INSERT INTO picture VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertAlbumPicture = "INSERT INTO albumpicture VALUES (default, ?, ?)";

        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(insertPicture,Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, picture.getFileName());
            pst.setString(2, picture.getFilepath());
            pst.setDouble(3, picture.getFileSize());
            Date d = picture.getDateTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(d);
            pst.setString(4, currentTime);
            pst.setInt(5, picture.getISO());
            pst.setInt(6, picture.getShutterSpeed());
            pst.setDouble(7, picture.getExposureTime());
            pst.setBoolean(8, picture.isFlashUsed());
            pst.setDouble(9, picture.getLatitude());
            pst.setDouble(10, picture.getLongitude());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if(rs.next())
            {
                int pictureId = rs.getInt(1);
                pst = conn.prepareStatement(insertAlbumPicture);
                pst.setInt(1, albumId);
                pst.setInt(2, pictureId);
                pst.executeUpdate();
            }

            return true;
        } catch(SQLException se) {
            System.out.println(se);
            return false;
        } finally {
            Database.closeConnection(conn, pst, result);
        }
    }

    public boolean deletePicture(Picture picture) {
        String query = "Delete from picture where id = ?";

        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, picture.getId());
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
