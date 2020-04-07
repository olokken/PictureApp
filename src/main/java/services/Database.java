package services;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String CONNECTION_STRING = "jdbc:mysql://mysql-ait.stud.idi.ntnu.no:3306/maritrov?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "maritrov"; // Skriv eget brukernavn som du har på mail fra ntnu databasegreier
    private static final String PASSWORD = "jHAPaoNi"; //Skriv eget passord som du har på mail fra ntnu databasegreier


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
            System.out.println(se);
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
            System.out.println(ex);
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
        }
        try {
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException se) {
        }
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException se) {
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
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se) {
        }
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException se) {
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
        }
        try {
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException se) {
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
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se) {
        }
    }
}
