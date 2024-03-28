package webserver.handler.requesthandler;

import config.Config;
import db.SessionStore;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;
import webserver.utils.SidUtils;

public class RootHandler implements HttpRequestHandler {
    private static final String COOKIE = "Cookie";
    private static final String DEFAULT_FILE = "index.html";

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
        if (isLoggedIn(httpRequest)) {
            DynamicFileHandler dynamicFileHandler = new DynamicFileHandler();
            return dynamicFileHandler.handleRequest(httpRequest);
        }
        StaticFileHandler staticFileHandler = new StaticFileHandler();
        return staticFileHandler.handleRequest(httpRequest);
    }

    private boolean isLoggedIn(HttpRequest httpRequest) {
        // index.html 파일에 대해서만 응답!
        if (!isDefaultFile(httpRequest)) {
            return false;
        }

        String cookie = httpRequest.getHeaderValue(COOKIE);

        try {
            String sid = SidUtils.getCookieSid(cookie);
            return isValidSid(sid);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValidSid(String sid) {
        return SessionStore.sessionIdExists(sid);
    }

    private boolean isDefaultFile(HttpRequest httpRequest) {
        return httpRequest.getRequestLine().endsWith(DEFAULT_FILE);
    }
}
