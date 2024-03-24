package webserver.handler.url;

import config.Config;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.WebServer;
import webserver.handler.UrlHandler;
import webserver.request.HttpRequest;
import webserver.request.message.HttpMethod;
import webserver.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;
import webserver.utils.QueryUtils;

import java.util.Map;

public class RegistrationHandler implements UrlHandler {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationHandler.class);

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, Config config) {
        HttpMethod httpMethod = httpRequest.getMethod();

        if (httpMethod.equals(HttpMethod.GET)) {
            return handleGetRequest(httpRequest, config);
        }

        return handlePostRequest(httpRequest, config);
    }

    private HttpResponse handleGetRequest(HttpRequest httpRequest, Config config) {
        try {
            String requestUrl = getRequestUrl(httpRequest);

            return createResponse(requestUrl, config);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return null;    // response 설정 실페 = 500 에러!
        }
    }

    private HttpResponse createResponse(String requestUrl, Config config) {
        byte[] responseBody = HttpResponseUtils.generateResponseBody(requestUrl, config);
        String responseStartLine = HttpResponseUtils.generateResponseStartLine(StatusCode.OK);
        String responseHeader = HttpResponseUtils.generateResponseHeader(responseBody.length, requestUrl);

        return new HttpResponse(responseStartLine, responseHeader, responseBody);
    }


    private String getRequestUrl(HttpRequest httpRequest) {
        String requestLine = httpRequest.getRequestLine();

        if (HttpRequestUtils.isUrl(requestLine)) {  // requestLine이 url 형식이라면
            return HttpRequestUtils.urlToStaticFile(requestLine);   // url + /index.html 로 넘겨준다.
        }

        if (HttpRequestUtils.isStaticFile(requestLine)) {   // requestLine이 staticFile이라면
            return requestLine; // 그대로 넘겨준다.
        }

        throw new IllegalArgumentException("[ERROR] 올바르지 않은 RequestLine 형식 : " + requestLine);
    }


    private HttpResponse handlePostRequest(HttpRequest httpRequest, Config config) {
        createUser(httpRequest.getBody());

        return createResponse();
    }

    private HttpResponse createResponse() {
        String responseStartLine = HttpResponseUtils.generateResponseStartLine(StatusCode.FOUND);
        String responseHeader = HttpResponseUtils.generateResponseHeader("/index.html");

        return new HttpResponse(responseStartLine, responseHeader);
    }

    private void createUser(String requestBody) {
        Map<String, String> queries = QueryUtils.getQueries(requestBody);
        User user = new User(queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email"));
        Database.addUser(user);
        logger.debug("user created : {}", user);
    }
}
