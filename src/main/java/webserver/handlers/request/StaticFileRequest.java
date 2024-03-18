package webserver.handlers.request;

import utils.RequestUtils;
import webserver.handlers.response.HttpResponse;
import webserver.handlers.response.StaticFileResponse;

public class StaticFileRequest extends HttpRequest {
    public StaticFileRequest(String startLine) {
        super(startLine);
    }

    @Override
    public HttpResponse sendResponse() {
        String requestTarget = RequestUtils.getRequestTarget(startLine);
        return new StaticFileResponse(requestTarget);
    }

}
