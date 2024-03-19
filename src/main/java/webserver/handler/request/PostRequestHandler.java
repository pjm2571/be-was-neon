package webserver.handler.request;

import webserver.request.HttpRequest;
import webserver.request.PostRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class PostRequestHandler extends RequestHandler {
    public PostRequestHandler(BufferedReader requestReader, String startLine) {
        super(requestReader, startLine);
    }

    @Override
    public HttpRequest getHttpRequest() throws IOException {
        Map<String, String> headers = getRequestHeader();
        return new PostRequest(startLine, headers, getRequestBody(headers));
    }

    private String getRequestBody(Map<String, String> headers) throws IOException {
        int length = Integer.parseInt(headers.get("Content-Length"));

        char[] body = new char[length];
        requestReader.read(body, 0, length);
        return new String(body);
    }


}
