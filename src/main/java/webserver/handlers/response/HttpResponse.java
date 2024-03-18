package webserver.handlers.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handlers.request.HttpRequest;

import java.util.Map;

public abstract class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private String startLine;
    private Map<String, String> headers;

    HttpResponse() {
        logger.debug("Response Object : {}", this.getClass().getSimpleName());
    }

    public void setStartLine(String startLine) {
        logger.debug("Response : {}", startLine);
        this.startLine = startLine;
    }

    public void setHeaders(Map<String, String> headers) {
        headers.forEach((key, value) -> logger.debug("Header : {} : {}", key, value));
        this.headers = headers;
    }


}
