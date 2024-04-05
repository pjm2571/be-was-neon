package webserver.handler.requesthandler;

import db.ArticleDataBase;
import db.SessionStore;
import model.User;
import webserver.handler.HttpRequestHandler;
import model.Article;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.sid.SidValidator;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;

import java.util.List;

public class RootHandler implements HttpRequestHandler {
    private static final String PATTERN = "<header(?:\\s+class=\"[^\"]*\")?>[\\s\\S]*?</header>[\\s\\S]*?<div class=\"wrapper\">";


    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) {
        switch (httpRequest.getMethod()) {
            case GET -> {
                return handleGet(httpRequest);
            }
            default -> {
                return HttpResponseUtils.get404Response();  // GET 요쳥이 아닌 경우
            }
        }
    }

    private HttpResponse handleGet(HttpRequest httpRequest) {
        httpRequest = HttpRequestUtils.convertToStaticFileRequest(httpRequest);
        if (SidValidator.isLoggedIn(httpRequest)) {
            DynamicFileHandler dynamicFileHandler = new DynamicFileHandler(httpRequest);
            String userName = getUserName(httpRequest);
            String replacement = getReplacement(userName);

            replacement += getArticles();

            return dynamicFileHandler.handleRequest(replacement, PATTERN);
        }
        StaticFileHandler staticFileHandler = new StaticFileHandler();
        return staticFileHandler.handleRequest(httpRequest);
    }

    private String getUserName(HttpRequest httpRequest) {
        String sid = SidValidator.getCookieSid(httpRequest);
        User user = SessionStore.getUserBySid(sid);
        return user.getName();
    }

    private String getReplacement(String userName) {

        return "<header class=\"header\">\n" +
                "<a href=\"/\"><img src=\"./img/signiture.svg\"></a>\n" +
                "<ul class=\"header__menu\">\n" +
                "    <li class=\"user-name\">" + userName + "</li>\n" +
                "    <li class=\"header__menu__item\">\n" +
                "        <a class=\"btn btn_contained btn_size_s\" href=\"/article\">글쓰기</a>\n" +
                "    </li>\n" +
                "<li class=\"header__menu__item\">\n" +
                "            <a class=\"btn btn_userlist btn_size_s\" href=\"/user/list\">유저 리스트</a>\n" +
                "          </li>" +
                "    <li class=\"header__menu__item\">\n" +
                "        <form id=\"logout-form\" action=\"/logout\" method=\"post\">\n" +
                "            <button type=\"submit\" class=\"btn btn_ghost btn_size_s\">로그아웃</button>\n" +
                "        </form>\n" +
                "    </li>\n" +
                "</ul>\n" +
                "</header>\n" +
                "<div class=\"wrapper\">\n";
    }

    private String getArticles() {
        StringBuilder sb = new StringBuilder();
        List<Article> articles = ArticleDataBase.getArticles();
        for (int i = articles.size() - 1; i >= 0; i--) {
            Article article = articles.get(i);
            sb.append(getArticleContent(article));
        }
        return sb.toString();
    }

    private String getArticleContent(Article article) {
        return "<div class=\"post\">\n" +
                "<div class=\"post__account\">\n" +
                "    <img class=\"post__account__img\">\n" +
                "    <p class=\"post__account__nickname\">" + article.getUserId() + "</p>\n" +
                "  </div>\n" +
                "  <img class=\"post__img\" src=\"" + article.getImgPath() + "\">\n" +
                "  <div class=\"post__menu\">\n" +
                "    <ul class=\"post__menu__personal\">\n" +
                "      <li>\n" +
                "        <button class=\"post__menu__btn\">\n" +
                "          <img src=\"./img/like.svg\">\n" +
                "        </button>\n" +
                "      </li>\n" +
                "      <li>\n" +
                "        <button class=\"post__menu__btn\">\n" +
                "          <img src=\"./img/sendLink.svg\">\n" +
                "        </button>\n" +
                "      </li>\n" +
                "    </ul>\n" +
                "    <button class=\"post__menu__btn\">\n" +
                "      <img src=\"./img/bookMark.svg\">\n" +
                "    </button>\n" +
                "  </div>\n" +
                "  <p class=\"post__article\">\n" +
                "    " + article.getContent() + "\n" +
                "  </p>\n";
    }
}
