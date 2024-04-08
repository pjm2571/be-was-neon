package db.memory;

import model.Article;

import java.util.LinkedHashMap;
import java.util.Map;

public class ArticleDatabase {
    private static int articleId = 0;
    private static Map<Integer, Article> articles = new LinkedHashMap<>();

    public static void addArticle(Article article) {
        articles.put(++articleId, article);
    }

    public static Map<Integer, Article> getArticles() {
        return articles;
    }

}
