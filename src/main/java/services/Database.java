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

public class Database {
    private static final String CONNECTION_STRING = "jdbc:mysql://mysql-ait.stud.idi.ntnu.no/olelok?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private final String USERNAME = getProperties().get("username").toString(); // Skriv eget brukernavn som du har på mail fra ntnu databasegreier
    private final String PASSWORD = getProperties().get("password").toString(); //Skriv eget passord som du har på mail fra ntnu databasegreier

    //Create logger object from PicLdLogger class.
    //private static PicLdLogger picLdLogger;

    private Map getProperties() {
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

    public Database() { }
    /**
     * Connectiong to database
     *
     * @return
     *        - a Connection-object.
     */
    public static Connection ConnectDB() {
        try {
            return DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
        } catch (SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
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
        } catch (SQLException ex) {
            //picLdLogger.getLogger().log(Level.FINE, ex.getMessage());
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
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
        }
        try {
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
        }
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
        }
    }
    /**
     * Closing connection
     *
     * @param conn
     *        - A given Connection-object.
     * @param result
     *        - A given ResultSet-object.
     * @param stmt
     *        - A given Statement-object.
     */
    public static void closeConnection(Connection conn, Statement stmt, ResultSet result) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
        }
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
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
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
        }
        try {
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException se) {
            //picLdLogger.getLogger().log(Level.FINE, se.getMessage());
        }
    }

    /**
     * Closing connection
     *
     * @param conn
     *        - A given Connection-object.
     * @param stmt
     *        - A given Statement-object.
     */
    public static void closeConnection(Connection conn, Statement stmt) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se) {
            AppLogger.getAppLogger().log(Level.FINE, se.getMessage());
            AppLogger.closeHandler();
        }
    }

    //public static void setPicLdLogger(PicLdLogger picLdLogger) {
        //Database.picLdLogger = picLdLogger;
    //}
}
