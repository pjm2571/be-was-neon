package webserver.handlers.request;

import utils.RequestUtils;
import webserver.handlers.response.HttpResponse;
import webserver.handlers.response.QueryResponse;

public class QueryRequest extends HttpRequest {
    public QueryRequest(String startLine) {
        super(startLine);
    }

    @Override
    public HttpResponse sendResponse() {
        String requestTarget = RequestUtils.getRequestTarget(startLine);
        return new QueryResponse(requestTarget);
    }
}
