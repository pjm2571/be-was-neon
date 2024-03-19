package webserver.request;

import webserver.utils.RequestUtils;
import webserver.response.HttpResponse;
import webserver.response.StaticFileResponse;

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
