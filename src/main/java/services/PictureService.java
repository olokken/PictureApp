package services;

import entities.Album;
import entities.Picture;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

public class PictureService {

    //Create logger object from PicLdLogger class.
    private PicLdLogger picLdLogger = new PicLdLogger();

    public PictureService() throws IOException { }

    public ArrayList<Picture> getAllPictures(int albumId, int userId) {
        String query;
        if (albumId >= 0) {
            query = "SELECT *  From picture as p INNER JOIN albumpicture as ap WHERE p.id = ap.pictureId and ap.albumId = ?";
        }
        else {
            query = "SELECT p.* From picture p " +
                    "join albumpicture ap on p.id = ap.pictureId " +
                    "join album a on a.id = ap.albumId " +
                    "where a.userid = ?";
        }

        ArrayList<Picture> pictures = new ArrayList<>();

        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(query);
            if (albumId >= 0) {
                pst.setInt(1,albumId);
            }
            else if (albumId < 0) {
                pst.setInt(1, userId);
            }
            result = pst.executeQuery();

            while(result.next()) {
                Picture pic = new Picture(result.getInt("id"), result.getString("fileName"), result.getString("filePath"),
                        result.getDouble("fileSize"), result.getDate("dateTaken"), result.getInt("iso"), result.getInt("shutterSpeed"), result.getDouble("exposureTime"),
                        result.getBoolean("isFlashUsed"), result.getDouble("latitude"), result.getDouble("longitude"));
                pic.setTags(getTags(pic));
                pictures.add(pic);
            }
                return pictures;
        } catch(SQLException | IOException se) {
            picLdLogger.getLogger().log(Level.FINE, se.getMessage());
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
            SimpleDateFormat sdf;
            String currentTime = null;
            if (d!= null) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                currentTime = sdf.format(d);
            }
            pst.setString(4, currentTime);
            pst.setInt(5, picture.getISO());
            pst.setInt(6, picture.getShutterSpeed());
            pst.setDouble(7, picture.getExposureTime());
            pst.setBoolean(8, picture.isFlashUsed());
            pst.setDouble(9, picture.getLatitude());
            pst.setDouble(10, picture.getLongitude());
            pst.executeUpdate();
            if (albumId != 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    int pictureId = rs.getInt(1);
                    pst = conn.prepareStatement(insertAlbumPicture);
                    pst.setInt(1, albumId);
                    pst.setInt(2, pictureId);
                    pst.executeUpdate();
                }
            }

            return true;
        } catch(SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
            return false;
        } finally {
            Database.closeConnection(conn, pst, result);
        }
    }

    public boolean deletePicture(int pictureid, int albumid) {
        String query = "Delete from picture where id = ?";
        String deleteFromAlbumQuery = "Delete from albumpicture where pictureid = ? and albumid = ?";
        String deleteFromAllAlbumsQuery = "Delete from albumpicture where pictureid = ?";

        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        try {
            if(albumid == 0) {
                pst = conn.prepareStatement(deleteFromAllAlbumsQuery);
            } else {
                pst = conn.prepareStatement(deleteFromAlbumQuery);
                pst.setInt(2, albumid);
            }
            pst.setInt(1, pictureid);
            pst.executeUpdate();

            pst = conn.prepareStatement(query);
            pst.setInt(1, pictureid);
            pst.executeUpdate();
            return true;
        } catch(SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
            return false;
        } finally {
            Database.closeConnection(conn, pst);
        }
    }

    public boolean addTags (Picture p, String tag) {
        String insertPictureTag = "INSERT INTO picturetag VALUES (default, ?, ?)";

        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(insertPictureTag,Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, p.getId());
            pst.setString(2, tag);
            pst.executeUpdate();
            return true;
        } catch(SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
            return false;
        } finally {
            Database.closeConnection(conn, pst, result);
        }
    }


    public ArrayList<String> getTags(Picture picture) {
        String query = "Select * From picturetag where pictureId = ?";
        ArrayList<String> tags = new ArrayList<>();

        Connection conn = Database.ConnectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, picture.getId());
            result = pst.executeQuery();

            while(result.next()) {
                tags.add(result.getString("tagName"));
            }
            return tags;
        } catch(SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
            return null;
        } finally {
            Database.closeConnection(conn, pst, result);
        }
    }


}
