package webserver.handler.urlhandler;

import config.Config;
import db.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.Url;
import webserver.constants.Constants;
import webserver.handler.UrlHandler;
import webserver.handler.errorhandler.ErrorHandler;
import webserver.handler.filehandler.StaticFileHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;
import webserver.utils.SidUtils;

public class MainHandler implements UrlHandler {
    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);


    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, Config config) {
        return switch (httpRequest.getMethod()) {
            case GET -> handleGet(httpRequest, config);
            case POST -> ErrorHandler.get404Response();
        };
    }

    private HttpResponse handleGet(HttpRequest httpRequest, Config config) {
        if (!isLoggedIn(httpRequest)) { // 로그인되어있지 않은 경우 접근한다면 root로 리다이렉트
            String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.FOUND);
            String header = HttpResponseUtils.generateRedirectResponseHeader(Constants.ROOT_URL);
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
            // Cookie가 없거나
            // 존재하지 않는 Sid로 접속하는 경우
            logger.error(e.getMessage());
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
