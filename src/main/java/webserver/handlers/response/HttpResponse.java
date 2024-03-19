package webserver.handlers.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handlers.StatusCode;

import java.util.Map;

public abstract class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private static final String CSRF = "\r\n";

    private String requestTarget;

    private String startLine;
    private String responseHeader;
    private byte[] responseBody;

    public HttpResponse(String requestTarget) {
        this.requestTarget = requestTarget;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public String getStartLine() {
        return startLine;
    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(byte[] body) {
        this.responseBody = body;
    }

    public void setResponseHeader(String mimeType, int bodyLength) {
        this.responseHeader = "Content-Type: " + mimeType + CSRF + "Content-Length: " + bodyLength + CSRF + CSRF;
    }

    public void setResponseHeader(){
        this.responseHeader = "Location: " + "/index.html" + CSRF + CSRF;
    }

    public void setStartLine(StatusCode statusCode) {
        this.startLine = "HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getDescription() + CSRF;
    }
}
