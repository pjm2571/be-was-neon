package webserver.request;

import webserver.response.HttpResponse;
import webserver.response.PostResponse;
import webserver.utils.RequestUtils;

public class PostRequest extends HttpRequest {

    public PostRequest(String startLine) {
        super(startLine);
    }

    @Override
    public HttpResponse sendResponse() {
        String requestTarget = RequestUtils.getRequestTarget(startLine);
        return new PostResponse(requestTarget, body);
    }
}
