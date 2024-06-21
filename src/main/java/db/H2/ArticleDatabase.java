package db.H2;

import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.utils.JdbcUtils;

import java.sql.*;
import java.util.*;

public class ArticleDatabase {
    private static final Logger logger = LoggerFactory.getLogger(ArticleDatabase.class);
    private static final String ARTICLE_INSERT = "INSERT INTO ARTICLES (USERID, CONTENT, IMG_PATH) VALUES (?, ?, ?)";
    private static final String ARTICLE_FIND_ALL = "SELECT * FROM ARTICLES";
    private static Connection conn = null;
    private static PreparedStatement stmt = null;
    private static ResultSet rs = null;

    public static void addArticle(Article article) {
        try {
            conn = JdbcUtils.getConnection();
            stmt = conn.prepareStatement(ARTICLE_INSERT);

            stmt.setString(1, article.getUserId());
            stmt.setString(2, article.getContent());
            stmt.setString(3, article.getImgPath());

            stmt.executeUpdate();
            logger.debug("article : {} 이 DB에 저장되었습니다.", article);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            JdbcUtils.close(stmt, conn);
        }
    }

    public static Map<Integer, Article> getArticles() {
        Map<Integer, Article> articles = new LinkedHashMap<>();
        try {
            conn = JdbcUtils.getConnection();
            stmt = conn.prepareStatement(ARTICLE_FIND_ALL);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int articleId = rs.getInt("ARTICLE_ID");
                String userId = rs.getString("USERID");
                String content = rs.getString("CONTENT");
                String img_path = rs.getString("IMG_PATH");
                Article article = new Article(userId, content, img_path);
                articles.put(articleId, article);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            JdbcUtils.close(rs, stmt, conn);
        }

        return articles;
    }

}
