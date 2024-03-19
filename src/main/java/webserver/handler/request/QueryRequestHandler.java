package webserver.handler.request;

import webserver.request.*;

import java.io.*;

public class QueryRequestHandler extends RequestHandler {
    public QueryRequestHandler(BufferedReader requestReader, String startLine) {
        super(requestReader, startLine);
    }

    @Override
    public HttpRequest getHttpRequest() throws IOException {
        return new QueryRequest(startLine, getRequestHeader());
    }

}
