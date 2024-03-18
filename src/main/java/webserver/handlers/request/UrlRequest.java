package webserver.handlers.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class UrlRequest extends HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(UrlRequest.class);

    public UrlRequest(String startLine) {
        super(startLine);
    }


}
