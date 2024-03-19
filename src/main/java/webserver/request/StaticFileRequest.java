package webserver.request;

import webserver.response.HttpResponse;

import java.util.Map;

public class StaticFileRequest extends HttpRequest {
    public StaticFileRequest(String startLine, Map<String, String> headers) {
        super(startLine, headers);
    }

}
