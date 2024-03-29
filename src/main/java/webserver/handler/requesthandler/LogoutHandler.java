package webserver.handler.requesthandler;

import db.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.sid.SidValidator;
import webserver.utils.HttpResponseUtils;

public class LogoutHandler implements HttpRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    private static final String ROOT_URL = "/";
    private static final String COOKIE = "Cookie";

    private static final String SPACE = " ";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) {
        switch (httpRequest.getMethod()) {
            case POST -> {
                return handlePost(httpRequest);
            }
            default -> {
                return HttpResponseUtils.get404Response();  // GET 요쳥이 아닌 경우
            }
        }
    }

    private HttpResponse handlePost(HttpRequest httpRequest) {
        String header = HttpResponseUtils.generateRedirectResponseHeader(ROOT_URL);
        String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.FOUND);
        try {
            String sid = SidValidator.getCookieSid(httpRequest);
            String expireSid = getExpireSid(sid);
            SessionStore.expireSid(sid);
            header = HttpResponseUtils.setCookieHeader(header, expireSid);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
        return new HttpResponse(startLine, header);
    }

    private String getExpireSid(String sid) {
        return sid + ";" + SPACE + "max-age=0";
    }
}
