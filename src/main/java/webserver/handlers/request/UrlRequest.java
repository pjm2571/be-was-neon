package webserver.handlers.request;

import webserver.handlers.response.HttpResponse;
import webserver.handlers.response.StaticFileResponse;

public class UrlRequest extends HttpRequest {
    public UrlRequest(String startLine) {
        super(startLine);
    }

    @Override
    public HttpResponse sendResponse() {
        return new StaticFileResponse();
    }

}
