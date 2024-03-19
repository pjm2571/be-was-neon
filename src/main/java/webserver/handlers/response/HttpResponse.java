package webserver.handlers.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handlers.StatusCode;

import java.util.Map;

public abstract class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private String requestTarget;

    private String startLine;
    private String responseHeader;
    private byte[] responseBody;

    public HttpResponse(String requestTarget) {
        this.requestTarget = requestTarget;
        logger.debug("Response Object : {}", this.getClass().getSimpleName());
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
        this.responseHeader = "Content-Type: " + mimeType + "\r\n" + "Content-Length: " + bodyLength + "\r\n" + "\r\n";
    }

    public void setStartLine(StatusCode statusCode) {
        this.startLine = "HTTP/1.1 " + statusCode.getCode() + " " + statusCode.getDescription();
    }
}
