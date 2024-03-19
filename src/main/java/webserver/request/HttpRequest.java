package webserver.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.response.HttpResponse;

import java.util.Map;

public abstract class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    protected String startLine;
    private Map<String, String> headers;

    HttpRequest(String startLine) {
        this.startLine = startLine;
    }

    public void setHeaders(Map<String, String> headers) {
        logger.debug("Request Object : {}", this.getClass().getSimpleName());
        logger.debug("Request : {}", startLine);
        headers.forEach((key, value) -> logger.debug("Header : {} : {}", key, value));
        this.headers = headers;
    }

    public abstract HttpResponse sendResponse();
}
