package webserver.handler.url;

import config.Config;
import db.SessionStore;
import webserver.StatusCode;
import webserver.Url;
import webserver.constants.Constants;
import webserver.handler.UrlHandler;
import webserver.handler.error.ErrorHandler;
import webserver.handler.file.StaticFileHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.SidUtils;

public class MainHandler implements UrlHandler {

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, Config config) {
        return switch (httpRequest.getMethod()) {
            case GET -> handleGet(httpRequest, config);
            case POST -> ErrorHandler.get404Response();
        };
    }

    private HttpResponse handleGet(HttpRequest httpRequest, Config config) {
        if (!isLoggedIn(httpRequest)) { // 로그인되어있지 않은 경우 접근한다면 root로 리다이렉트
            String startLine = HttpRequestUtils.generateResponseStartLine(StatusCode.FOUND);
            String header = HttpRequestUtils.generateRedirectResponseHeader(Constants.ROOT_URL);
            return new HttpResponse(startLine, header);
        }
        if (httpRequest.getRequestLine().equals(Url.MAIN.getUrlPath())) {   // url경로와 같다면 staticFileHandler!
            StaticFileHandler staticFileHandler = new StaticFileHandler();
            return staticFileHandler.handleRequest(HttpRequestUtils.convertToStaticFileRequest(httpRequest), config);
        }
        return ErrorHandler.get404Response();   // URL 이외의 요청은 404!
    }

    private boolean isLoggedIn(HttpRequest httpRequest) {
        try {
            String sid = getSid(httpRequest.getHeaderValue(Constants.COOKIE));
            return SessionStore.sessionIdExists(sid);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private String getSid(String cookie) {
        if (cookie == null) {
            throw new IllegalArgumentException("[ERROR] 쿠키가 없습니다.");
        }
        return SidUtils.getCookieSid(cookie);
    }
}
