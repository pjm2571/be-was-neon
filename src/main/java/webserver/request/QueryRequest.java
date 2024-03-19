package webserver.request;

import webserver.utils.RequestUtils;
import webserver.response.HttpResponse;
import webserver.response.QueryResponse;

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
