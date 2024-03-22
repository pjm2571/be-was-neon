package webserver.handler.request;

import webserver.request.HttpRequest;
import webserver.request.StaticFileRequest;
import webserver.utils.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;

public class UrlRequestHandler extends RequestHandler {
    private static final String DEFAULT_FILE = "/index.html";

    public UrlRequestHandler(BufferedReader requestReader, String startLine) {
        super(requestReader, startLine);
    }

    @Override
    public HttpRequest getHttpRequest() throws IOException {
        return new StaticFileRequest(replaceStartLine(), getRequestHeader());
    }

    private String replaceStartLine() {
        String oldRequestTarget = HttpRequestUtils.getRequestTarget(startLine);
        String newRequestTarget = oldRequestTarget + DEFAULT_FILE;
        return startLine.replace(oldRequestTarget, newRequestTarget);
    }


}
