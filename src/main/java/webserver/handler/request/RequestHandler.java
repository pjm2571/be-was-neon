package webserver.handler.request;

import webserver.request.HttpRequest;
import webserver.utils.HeaderUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class RequestHandler {
    protected BufferedReader requestReader;
    protected String startLine;

    public RequestHandler(BufferedReader requestReader, String startLine) {
        this.requestReader = requestReader;
        this.startLine = startLine;
    }

    public abstract HttpRequest getHttpRequest() throws IOException;

    protected Map<String, String> getRequestHeader() throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();

        StringBuilder headerLine = new StringBuilder(requestReader.readLine());
        while (!headerLine.toString().isEmpty()) {
            headers.put(HeaderUtils.getHeaderKey(headerLine.toString()), HeaderUtils.getHeaderValue(headerLine.toString()));
            headerLine.replace(0, headerLine.length(), "");
            headerLine.append(requestReader.readLine());
        }

        return headers;
    }
}
