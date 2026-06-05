package util;
import java.sql.*;

public class DBConnection {
    // ─── CHANGE THESE TWO LINES TO MATCH YOUR MYSQL SETUP ───────────────────
    private static final String USER = "root";       // your MySQL username
    private static final String PASS = "anaskhan16."; // your MySQL password
    // ─────────────────────────────────────────────────────────────────────────

    private static final String URL =
        "jdbc:mysql://localhost:3306/banking_db" +
        "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL, USER, PASS);
        }
        return conn;
    }
}
