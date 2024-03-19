package webserver.request;

import webserver.utils.RequestUtils;
import webserver.response.HttpResponse;
import webserver.response.StaticFileResponse;

public class UrlRequest extends HttpRequest {
    public UrlRequest(String startLine) {
        super(startLine);
    }

    @Override
    public HttpResponse sendResponse() {
        String requestTarget = RequestUtils.getRequestTarget(startLine) + "/index.html";
        return new StaticFileResponse(requestTarget);
    }

}
