package services;

import entities.Picture;
import idk.AppLogger;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * Holds details about the usage of picture.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class PictureService {

    /**
     * Constructor that creates an instance of PictureService, initialising the instance.
     */
    public PictureService(){
    }

    /**
     * Returns all the pictures from a user from the database.
     * If the database won't connect, can't get user or album ID or execute, a
     * {@link SQLException} is thrown.
     *
     * @param albumId The album ID.
     * @param userId The user ID.
     * @return List of pictures.
     */
    public List<Picture> getAllPictures(int albumId, int userId) {
        String query;
        if (albumId >= 0) {
            query = "SELECT *  From picture as p INNER JOIN albumpicture as ap WHERE p.id = ap.pictureId and ap.albumId = ?";
        }
        else {
            query = "SELECT DISTINCT p.* From picture p " +
                    "join albumpicture ap on p.id = ap.pictureId " +
                    "join album a on a.id = ap.albumId " +
                    "where a.userid = ?";
        }

        ArrayList<Picture> pictures = new ArrayList<>();

        Connection conn = Database.connectDB();
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
        } catch(SQLException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
            return new ArrayList<>();
        } finally {
            Database.closeConnection(conn, pst, result);
        }
    }

    /**
     * Creates a picture in the database with the given album ID.
     * If the database won't connect, can't get picture or album ID, or execute,
     * a {@link SQLException} is thrown.
     *
     * @param picture The picture.
     * @param albumId The album ID.
     * @return True if picture is created.
     */
    public boolean createPicture(Picture picture, int albumId) {
        String insertPicture = "INSERT INTO picture VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertAlbumPicture = "INSERT INTO albumpicture VALUES (default, ?, ?)";
        String checkPictureQuery = "Select * from picture where filePath = ?";
        int pictureId = 0;

        Connection conn = Database.connectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(checkPictureQuery);
            pst.setString(1, picture.getFilepath());
            result = pst.executeQuery();
            if(!result.next()) {
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
                pst.setInt(5, picture.getIso());
                pst.setInt(6, picture.getShutterSpeed());
                pst.setDouble(7, picture.getExposureTime());
                pst.setBoolean(8, picture.isFlashUsed());
                pst.setDouble(9, picture.getLatitude());
                pst.setDouble(10, picture.getLongitude());
                pst.executeUpdate();
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    pictureId = rs.getInt(1);
                }
            } else {
                pictureId = result.getInt(1);
            }

            pst = conn.prepareStatement(insertAlbumPicture);
            pst.setInt(1, albumId);
            pst.setInt(2, pictureId);
            pst.executeUpdate();
            return true;
        } catch(SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
            return false;
        } finally {
            Database.closeConnection(conn, pst, result);
        }
    }

    /**
     * Delete a album from the given picture ID and album ID.
     * If the database won't connect, can't get picture ID or album ID, or execute,
     * a {@link SQLException} is thrown.
     *
     * @param pictureid The picture ID.
     * @param albumid The album ID.
     * @return True if picture is deleted.
     */
    public boolean deletePicture(int pictureid, int albumid) {
        String query = "Delete from picture where id = ?";
        String deleteFromAlbumQuery = "Delete from albumpicture where pictureid = ? and albumid = ?";
        String deleteFromPictureTagQuery = "Delete from picturetag where pictureid = ?";
        String checkPictureQuery = "Select * from albumpicture where pictureid = ?";


        Connection conn = Database.connectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(deleteFromAlbumQuery);
            pst.setInt(1, pictureid);
            pst.setInt(2, albumid);
            pst.executeUpdate();

            pst = conn.prepareStatement(checkPictureQuery);
            pst.setInt(1, pictureid);
            result = pst.executeQuery();
            if(!result.next()) {
                pst = conn.prepareStatement(deleteFromPictureTagQuery);
                pst.setInt(1, pictureid);
                pst.executeUpdate();

                pst = conn.prepareStatement(query);
                pst.setInt(1, pictureid);
                pst.executeUpdate();
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

    /**
     * Adds tag to a picture.
     * If the database won't connect, can't get picture or tag, or execute,
     * a {@link SQLException} is thrown.
     *
     * @param picture The picture.
     * @param tag The tag.
     * @return True if tag is added.
     */
    public boolean addTag(Picture picture, String tag) {
        String insertPictureTag = "INSERT INTO picturetag VALUES (default, ?, ?)";

        Connection conn = Database.connectDB();
        PreparedStatement pst = null;
        ResultSet result = null;
        try {
            pst = conn.prepareStatement(insertPictureTag,Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, picture.getId());
            pst.setString(2, tag);
            pst.executeUpdate();
            return true;
        } catch(SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
            return false;
        } finally {
            Database.closeConnection(conn, pst, result);
        }
    }

    /**
     * Returns list of tags belonging to the given picture.
     * If the database won't connect, can't get picture or execute,
     * a {@link SQLException} is thrown.
     *
     * @param picture The picture.
     * @return All the tags as a List.
     */
    public List<String> getTags(Picture picture) {
        String query = "Select * From picturetag where pictureId = ?";
        ArrayList<String> tags = new ArrayList<>();

        Connection conn = Database.connectDB();
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
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
            return new ArrayList<>();
        } finally {
            Database.closeConnection(conn, pst, result);
        }
    }

    /**
     * Delete tag to a picture.
     * If the database won't connect, can't get picture or tag, or execute,
     * a {@link SQLException} is thrown.
     *
     * @param picture The picture.
     * @param tag The tag.
     * @return True if tag is deleted.
     */
    public boolean deleteTag(Picture picture, String tag) {
        String query = "Delete from picturetag where pictureId = ? and tagName = ?";

        Connection conn = Database.connectDB();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, picture.getId());
            pst.setString(2, tag);
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
