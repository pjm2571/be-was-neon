package webserver.handlers.request;

import java.util.Map;

public abstract class HttpRequest {
    private String startLine;
    protected Map<String, String> headers;

    HttpRequest(String startLine) {
        this.startLine = startLine;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
