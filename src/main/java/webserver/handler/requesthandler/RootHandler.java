package webserver.handler.requesthandler;

import db.SessionStore;
import model.User;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.sid.SidValidator;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;

public class RootHandler implements HttpRequestHandler {
    private static final String COOKIE = "Cookie";
    private static final String DEFAULT_FILE = "index.html";
    private static final String PATTERN = "<ul(?:\\s+class=\"[^\"]*\")?>[\\s\\S]*?</ul>";

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

        return "<ul class=\"header__menu\">\n" +
                "    <li class=\"user-name\">" + userName + "</li>\n" +
                "    <li class=\"header__menu__item\">\n" +
                "        <a class=\"btn btn_contained btn_size_s\" href=\"/article\">글쓰기</a>\n" +
                "    </li>\n" +
                "<li class=\"header__menu__item\">\n" +
                "            <a class=\"btn btn_userlist btn_size_s\" href=\"/user/list\">유저 리스트</a>\n" +
                "          </li>"+
                "    <li class=\"header__menu__item\">\n" +
                "        <form id=\"logout-form\" action=\"/logout\" method=\"post\">\n" +
                "            <button type=\"submit\" class=\"btn btn_ghost btn_size_s\">로그아웃</button>\n" +
                "        </form>\n" +
                "    </li>\n" +
                "</ul>";
    }
}
