package webserver.handler.url;

import config.Config;
import db.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.Url;
import webserver.constants.Constants;
import webserver.handler.UrlHandler;
import webserver.handler.error.ErrorHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;
import webserver.utils.SidUtils;

public class LogoutHandler implements UrlHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, Config config) {
        return switch (httpRequest.getMethod()) {
            case GET -> handleGet(httpRequest, config);
            case POST -> ErrorHandler.get404Response();
        };
    }

    private HttpResponse handleGet(HttpRequest httpRequest, Config config) {
        if (httpRequest.getRequestLine().equals(Url.LOGOUT.getUrlPath())) {
            return logoutUser(httpRequest);
        }
        return ErrorHandler.get404Response();   // URL 이외의 요청은 404!
    }

    private HttpResponse logoutUser(HttpRequest httpRequest) {
        String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.FOUND);
        String header = validateCookie(httpRequest);
        return new HttpResponse(startLine, header);
    }

    private String validateCookie(HttpRequest httpRequest) {
        try {
            String sid = getSid(httpRequest.getHeaderValue(Constants.COOKIE));
            // 1) 헤더에 쿠키값을 만료시켜 보내야 한다.
            String expireSid = getExpireSid(sid);
            // 2) Session.Store에서 sid를 삭제해야 한다.
            SessionStore.expireSid(sid);

            String header = HttpResponseUtils.generateRedirectResponseHeader(Constants.ROOT_URL);
            header = HttpResponseUtils.setCookieHeader(header, expireSid);

            return header;
        } catch (IllegalArgumentException e) {
            // 1) 쿠키가 없는 경우
            // 2) 저장된 세션 중에 sid가 없는 경우
            logger.error(e.getMessage());
            return HttpResponseUtils.generateRedirectResponseHeader(Constants.ROOT_URL);
        }
    }

    private String getSid(String cookie) {
        if (cookie == null) {
            throw new IllegalArgumentException("[ERROR] 쿠키가 없습니다.");
        }
        return SidUtils.getCookieSid(cookie);
    }

    private String getExpireSid(String sid) {
        return sid + ";" + Constants.SPACE + "max-age=0";
    }

}
