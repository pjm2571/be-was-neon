package webserver.request;

import webserver.response.HttpResponse;

import java.util.Map;

public class QueryRequest extends HttpRequest {
    public QueryRequest(String startLine, Map<String, String> headers) {
        super(startLine, headers);
    }

}
