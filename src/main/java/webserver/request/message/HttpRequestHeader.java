package webserver.request.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;

import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestHeader {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHeader.class);

    private final Map<String, String> headers;

    public HttpRequestHeader(Map<String, String> headers) {
        this.headers = headers;
        headers.forEach((key, value) -> logger.debug("Header : {} : {}", key, value));
    }

    public String getValue(String key) {
        return headers.get(key);
    }

}
