package webserver.handlers.request;

import webserver.handlers.response.HttpResponse;
import webserver.handlers.response.QueryResponse;

public class QueryRequest extends HttpRequest {
    public QueryRequest(String startLine) {
        super(startLine);
    }

    @Override
    public HttpResponse sendResponse() {
        return new QueryResponse();
    }
}
