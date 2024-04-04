package db;

import webserver.handler.inputhandler.Article;

import java.util.LinkedList;
import java.util.List;

public class ArticleDataBase {
    private static List<Article> articles = new LinkedList<>();

    public static void addArticle(Article article) {
        articles.add(article);
    }

    public static List<Article> getArticles() {
        return articles;
    }

}
