package webserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.requesthandler.ArticleHandler;

import java.sql.*;

public class JdbcUtils {
    private static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);
    private static final String DATABASE_URL = "jdbc:h2:tcp://localhost/~/was";
    private static final String USERID = "sa";
    private static final String USERPW = "";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            DriverManager.registerDriver(new org.h2.Driver());
            connection = DriverManager.getConnection(DATABASE_URL, USERID, USERPW);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return connection;
    }

    public static void close(PreparedStatement stmt, Connection conn) {
        try {
            stmt.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        try {
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static void close(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            rs.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        try {
            stmt.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        try {
            conn.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
