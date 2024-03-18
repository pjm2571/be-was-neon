package webserver.handlers.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handlers.parsers.RequestParser;

import java.util.Map;

public class QueryRequest extends HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(QueryRequest.class);

    public QueryRequest(String startLine) {
        super(startLine);
    }

}
