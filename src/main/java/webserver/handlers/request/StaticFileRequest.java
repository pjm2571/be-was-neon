package webserver.handlers.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class StaticFileRequest extends HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(StaticFileRequest.class);


    public StaticFileRequest(String startLine) {
        super(startLine);
    }

}
