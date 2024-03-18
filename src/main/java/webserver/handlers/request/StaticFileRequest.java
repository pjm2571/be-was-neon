package webserver.handlers.request;

import webserver.handlers.response.HttpResponse;
import webserver.handlers.response.StaticFileResponse;

public class StaticFileRequest extends HttpRequest {
    public StaticFileRequest(String startLine) {
        super(startLine);
    }

    @Override
    public HttpResponse sendResponse() {
        return new StaticFileResponse();
    }

}
