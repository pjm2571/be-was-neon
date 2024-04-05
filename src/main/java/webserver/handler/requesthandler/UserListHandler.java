package webserver.handler.requesthandler;

import db.Database;
import db.H2.UserDatabase;
import db.SessionStore;
import model.User;
import webserver.StatusCode;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.sid.SidValidator;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;

import java.util.StringJoiner;

public class UserListHandler implements HttpRequestHandler {
    private static final String REDIRECT_URL = "/login";
    private static final String COOKIE = "Cookie";

    private static final String PATTERN = "<ul(?:\\s+class=\"ul-user\"*\")?>[\\s\\S]*?</ul>";

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
            String replacement = getAllUser();
            return dynamicFileHandler.handleRequest(replacement, PATTERN);
        }
        String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.FOUND);
        String header = HttpResponseUtils.generateRedirectResponseHeader(REDIRECT_URL);
        return new HttpResponse(startLine, header);
    }



    private String getAllUser() {
        StringJoiner joiner = new StringJoiner("\n");
        for (User user : UserDatabase.findAll()) {
            joiner.add("<li>" + user.getName() + "</li>");
        }
        return joiner.toString();
    }

}
