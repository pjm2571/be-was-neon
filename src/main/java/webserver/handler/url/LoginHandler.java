package webserver.handler.url;

import config.Config;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.Url;
import webserver.constants.Constants;
import webserver.handler.UrlHandler;
import webserver.handler.error.ErrorHandler;
import webserver.handler.file.StaticFileHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.sid.SidGenerator;
import webserver.utils.HttpRequestUtils;
import webserver.utils.QueryUtils;

import java.util.Arrays;
import java.util.Map;

public class LoginHandler implements UrlHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    private enum LoginUrl {
        loginUser
    }

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, Config config) {
        return switch (httpRequest.getMethod()) {
            case GET -> handleGet(httpRequest, config);
            case POST -> handlePost(httpRequest, config);
        };
    }

    private HttpResponse handleGet(HttpRequest httpRequest, Config config) {
        if (httpRequest.getRequestLine().equals(Url.LOGIN.getUrlPath())) {   // url경로와 같다면 staticFileHandler!
            StaticFileHandler staticFileHandler = new StaticFileHandler();
            return staticFileHandler.handleRequest(HttpRequestUtils.convertToStaticFileRequest(httpRequest), config);
        }
        return ErrorHandler.get404Response();   // URL 이외의 요청은 404!
    }

    private boolean isValidUrl(String url) {
        return Arrays.stream(LoginUrl.values())
                .anyMatch(registrationUrl -> registrationUrl.name().equals(url));
    }

    private String getLoginUrl(String requestLine) {
        String loginUrl = requestLine.replaceFirst(Url.LOGIN.getUrlPath(), "");
        return loginUrl.startsWith("/") ? loginUrl.substring(1) : loginUrl; // 첫 번째 문자열이 "/"로 시작하는지 확인하고 제거
    }

    private HttpResponse handlePost(HttpRequest httpRequest, Config config) {
        String loginUrl = getLoginUrl(httpRequest.getRequestLine());

        if (isValidUrl(loginUrl)) {
            // login
            return loginUser(httpRequest);
        }
        return ErrorHandler.get404Response();
    }

    private HttpResponse loginUser(HttpRequest httpRequest) {
        Map<String, String> queries = QueryUtils.getQueries(httpRequest.getBody());

        String inputId = queries.get("userId");
        String inputPassword = queries.get("password");
        User user = Database.findUserById(inputId);

        String startLine = HttpRequestUtils.generateResponseStartLine(StatusCode.FOUND);
        String header;
        try {
            validateUser(user, inputId, inputPassword);
            String sid = SidGenerator.getRandomSid();
            header = HttpRequestUtils.generateRedirectResponseHeader(Constants.LOGIN_SUCCESS_URL, sid);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            header = HttpRequestUtils.generateRedirectResponseHeader(Constants.LOGIN_FAIL_URL);
        }

        return new HttpResponse(startLine, header);
    }

    private void validateUser(User user, String inputId, String inputPassword) {
        if (user == null) {
            throw new IllegalArgumentException("[ERROR] 등록되지 않은 사용자입니다.");
        }
        if (!(user.getPassword().equals(inputPassword) && user.getUserId().equals(inputId))) {
            throw new IllegalArgumentException("[ERROR] 비밀번호가 일치하지 않습니다.");
        }
    }
}
