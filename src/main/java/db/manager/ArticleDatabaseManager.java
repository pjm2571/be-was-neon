package db.manager;

import model.Article;

import java.util.Map;

public class ArticleDatabaseManager {
    public static void addArticleDB(Article article) {
        db.H2.ArticleDatabase.addArticle(article);
    }

    public static Map<Integer, Article> getArticlesDB() {
        return db.H2.ArticleDatabase.getArticles();
    }

    public static void addArticleMEM(Article article) {
        db.memory.ArticleDatabase.addArticle(article);
    }

    public static Map<Integer, Article> getArticlesMEM() {
        return db.memory.ArticleDatabase.getArticles();
    }

}
