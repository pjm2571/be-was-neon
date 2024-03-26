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
import webserver.utils.HttpRequestUtils;
import webserver.utils.QueryUtils;

import java.util.Arrays;
import java.util.Map;

public class RegistrationHandler implements UrlHandler {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationHandler.class);

    private enum RegistrationUrl {
        createUser
    }

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, Config config) {
        return switch (httpRequest.getMethod()) {
            case GET -> handleGet(httpRequest, config);
            case POST -> handlePost(httpRequest, config);
        };
    }

    private HttpResponse handleGet(HttpRequest httpRequest, Config config) {
        if (httpRequest.getRequestLine().equals(Url.REGISTRATION.getUrlPath())) {   // url경로와 같다면 staticFileHandler!
            StaticFileHandler staticFileHandler = new StaticFileHandler();
            return staticFileHandler.handleRequest(HttpRequestUtils.convertToStaticFileRequest(httpRequest), config);
        }
        return ErrorHandler.get404Response();   // URL 이외의 요청은 404!
    }

    private HttpResponse handlePost(HttpRequest httpRequest, Config config) {
        String registrationUrl = getRegistrationUrl(httpRequest.getRequestLine());

        if (isValidUrl(registrationUrl)) {
            createUser(httpRequest);    // 유저 생성
            String startLine = HttpRequestUtils.generateResponseStartLine(StatusCode.FOUND);
            String header = HttpRequestUtils.generateRedirectResponseHeader(Constants.ROOT_URL);
            return new HttpResponse(startLine, header);
        }
        return ErrorHandler.get404Response();
    }

    private boolean isValidUrl(String url) {
        return Arrays.stream(RegistrationUrl.values())
                .anyMatch(registrationUrl -> registrationUrl.name().equals(url));
    }

    private String getRegistrationUrl(String requestLine) {
        String registrationUrl = requestLine.replaceFirst(Url.REGISTRATION.getUrlPath(), "");
        return registrationUrl.startsWith("/") ? registrationUrl.substring(1) : registrationUrl; // 첫 번째 문자열이 "/"로 시작하는지 확인하고 제거
    }

    private void createUser(HttpRequest httpRequest) {
        Map<String, String> queries = QueryUtils.getQueries(httpRequest.getBody());
        User user = new User(queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email"));
        Database.addUser(user);
        logger.debug("user created : {}", user);
    }

}
