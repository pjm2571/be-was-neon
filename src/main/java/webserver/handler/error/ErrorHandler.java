package webserver.handler.error;

import webserver.StatusCode;
import webserver.response.HttpResponse;
import webserver.utils.HttpRequestUtils;

import static webserver.constants.Constants.*;

public class ErrorHandler {
    public static HttpResponse get404Response() {
        byte[] responseBody = "<h1>404 Not Found</h1>".getBytes(); // 404 오류 페이지 반환
        String responseHeader = HttpRequestUtils.generateErrorResponseHeader(responseBody.length);
        String startLine = HttpRequestUtils.generateResponseStartLine(StatusCode.NOT_FOUND);
        return new HttpResponse(startLine, responseHeader, responseBody);
    }

    public static HttpResponse get500Response() {
        byte[] responseBody = "<h1>500 Internal Server Error</h1>".getBytes(); // 서버 오류 페이지 반환
        String responseHeader = HttpRequestUtils.generateErrorResponseHeader(responseBody.length);
        String startLine = HttpRequestUtils.generateResponseStartLine(StatusCode.INTERNAL_SERVER_ERROR);
        return new HttpResponse(startLine, responseHeader, responseBody);
    }

}
