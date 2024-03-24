package webserver.handler.url;

import config.Config;
import webserver.StatusCode;
import webserver.handler.UrlHandler;
import webserver.request.HttpRequest;
import webserver.request.message.HttpMethod;
import webserver.response.HttpResponse;
import webserver.utils.HttpRequestUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.utils.HttpResponseUtils;

import java.util.Objects;

public class RootHandler implements UrlHandler {
    private static final Logger logger = LoggerFactory.getLogger(RootHandler.class);

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, Config config) {
        HttpMethod httpMethod = httpRequest.getMethod();

        return handleGetRequest(httpRequest, config);  // GET 요청밖에 없으므로 if문 불필요
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


}
