package webserver.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.utils.HttpRequestUtils;

import java.util.Map;

public abstract class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    protected String startLine;
    protected Map<String, String> headers;
    protected String body;

    public HttpRequest(String startLine, Map<String, String> headers) {
        this.startLine = startLine;
        this.headers = headers;
        logger.debug("Request StartLine : {}", startLine);
        headers.forEach((key, value) -> logger.debug("Header : {} : {}", key, value));
    }

    public HttpRequest(String startLine, Map<String, String> headers, String body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
        logger.debug("Request StartLine : {}", startLine);
        headers.forEach((key, value) -> logger.debug("Header : {} : {}", key, value));
        logger.debug("Request Body : {}", body);
    }

    public String getRequestTarget() {
        return HttpRequestUtils.getRequestTarget(startLine);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

}
