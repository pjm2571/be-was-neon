package db.H2;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.utils.JdbcUtils;


import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDatabase {
    private static final Logger logger = LoggerFactory.getLogger(UserDatabase.class);
    private static final String USER_INSERT = "INSERT INTO USERS VALUES(?, ?, ?, ?)";
    private static final String FIND_USER_BY_ID = "SELECT * FROM USERS WHERE USERID = ?";
    private static final String FIND_ALL_USER = "SELECT * FROM USERS";
    private static Connection conn = null;
    private static PreparedStatement stmt = null;
    private static ResultSet rs = null;

    public static void addUser(User user) {
        try {
            conn = JdbcUtils.getConnection();
            stmt = conn.prepareStatement(USER_INSERT);

            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getEmail());
            stmt.executeUpdate();

            logger.debug("user : {} 가 DB에 저장되었습니다.", user);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            JdbcUtils.close(stmt, conn);
        }
    }

    public static User findUserById(String userId) {
        User user = null;
        try {
            conn = JdbcUtils.getConnection();
            stmt = conn.prepareStatement(FIND_USER_BY_ID);
            stmt.setString(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("USERID");
                String pw = rs.getString("PASSWORD");
                String name = rs.getString("NAME");
                String email = rs.getString("EMAIL");

                user = new User(id, pw, name, email);
                return user;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            JdbcUtils.close(rs, stmt, conn);
        }
        return user;
    }

    public static Collection<User> findAll() {
        List<User> userList = new ArrayList<>();

        try {
            conn = JdbcUtils.getConnection();
            stmt = conn.prepareStatement(FIND_ALL_USER);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("USERID");
                String pw = rs.getString("PASSWORD");
                String name = rs.getString("NAME");
                String email = rs.getString("EMAIL");

                User user = new User(id, pw, name, email);
                userList.add(user);
            }
        } catch (SQLException e) {
            logger.error("Error occurred while finding all users: {}", e.getMessage());
        } finally {
            JdbcUtils.close(rs, stmt, conn);
        }

        return userList;
    }
}
