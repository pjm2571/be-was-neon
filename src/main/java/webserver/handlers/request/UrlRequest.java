package webserver.handlers.request;

import utils.RequestUtils;
import webserver.handlers.response.HttpResponse;
import webserver.handlers.response.StaticFileResponse;

public class UrlRequest extends HttpRequest {
    public UrlRequest(String startLine) {
        super(startLine);
    }

    @Override
    public HttpResponse sendResponse() {
        String requestTarget = RequestUtils.getRequestTarget(startLine);
        return new StaticFileResponse(requestTarget);
    }

}
