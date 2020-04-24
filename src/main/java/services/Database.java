package services;

import idk.AppLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Holds details about the database.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class Database {
    private static final String CONNECTION_STRING = "jdbc:mysql://mysql-ait.stud.idi.ntnu.no/olelok?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USERNAME = getProperties().get("username").toString();
    private static final String PASSWORD = getProperties().get("password").toString();

    /**
     * Constructor that creates an instance of database, initialising the instance.
     */
    private Database() {
    }

    /**
     * Returns a connection to the database.
     * If the database won't connect, a
     * {@link SQLException} is thrown.
     *
     * @return A connection object.
     */
    public static Connection connectDB() {
        try {
            return DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
        } catch (SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
            return null;
        }
    }

    /**
     * Closing the connection to the database.
     * If one of the parameters is null, a
     * {@link SQLException} is thrown.
     *
     * @param conn A given Connection-object.
     * @param result A given ResultSet-object.
     * @param pst A given PreparedStatement-object.
     */
    public static void closeConnection(Connection conn, PreparedStatement pst, ResultSet result) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
        }
        try {
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
        }
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
        }
    }
    /**
     * Closing the connection to the database.
     * If one of the parameters is null, a
     * {@link SQLException} is thrown.
     *
     * @param conn A given Connection-object.
     * @param pst A given PreparedStatement-object.
     */
    public static void closeConnection(Connection conn, PreparedStatement pst) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
        }
        try {
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
        }
    }

    /**
     * Returns a hashmap with the username and password of
     * the database.
     * If the argument of the file input stream is incorrect or
     * properties can't load, a {@link IOException} will be thrown.
     *
     * @return Hashmap with the username and password.
     */
    private static Map getProperties() {
        Map result = new HashMap();
        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            result.put("username", prop.getProperty("USERNAME"));
            result.put("password", prop.getProperty("PASSWORD"));
            return result;
        } catch (IOException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
            return null;
        }
    }
}
