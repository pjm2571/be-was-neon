package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.StringUtils;

import java.util.ArrayList;

public class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);

    private String httpMethod;
    private String requestTarget;
    private String httpVersion;
    private ArrayList<String> requestHeaders;

    Request(String requestLine, ArrayList<String> requestHeaders) {
        this.httpMethod = StringUtils.getHttpMethod(requestLine);
        this.requestTarget = StringUtils.getRequestTarget(requestLine);
        this.httpVersion = StringUtils.getHttpVersion(requestLine);
        logger.debug("request : {}", requestLine);

        this.requestHeaders = requestHeaders;
        requestHeaders.forEach(r -> logger.debug("header : {}", r));
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

}
