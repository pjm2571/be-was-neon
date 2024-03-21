package webserver.handler.request;

import webserver.request.HttpRequest;
import webserver.request.StaticFileRequest;

import java.io.BufferedReader;
import java.io.IOException;

public class StaticFileRequestHandler extends RequestHandler {

    public StaticFileRequestHandler(BufferedReader requestReader, String startLine) {
        super(requestReader, startLine);
    }

    @Override
    public HttpRequest getHttpRequest() throws IOException {
        return new StaticFileRequest(startLine, getRequestHeader());
    }

}
