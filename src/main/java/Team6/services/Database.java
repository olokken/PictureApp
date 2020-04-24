package Team6.services;

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

public class Database {
    private static final String CONNECTION_STRING = "jdbc:mysql://mysql-ait.stud.idi.ntnu.no/olelok?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USERNAME = getProperties().get("username").toString();
    private static final String PASSWORD = getProperties().get("password").toString();


    private static Map getProperties() {
        Map result = new HashMap();
        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            result.put("username", prop.getProperty("USERNAME"));
            result.put("password", prop.getProperty("PASSWORD"));
        } catch (IOException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
        return result;
    }

    private Database() { }

    /**
     * Connectiong to database
     *
     * @return
     *        - a Connection-object.
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
     * Rollback query
     *
     * @param conn
     *        - The given connection which the rollback is set on.
     */
    public static void rollBack(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
        }
    }


    /**
     * Closing connection
     *
     * @param conn
     *        - A given Connection-object.
     * @param result
     *        - A given ResultSet-object.
     * @param pst
     *        - A given PreparedStatement-object.
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
     * Closing connection
     *
     * @param conn
     *        - A given Connection-object.
     * @param pst
     *        - A given PreparedStatement-object.
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
}
