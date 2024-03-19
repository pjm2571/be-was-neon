package webserver.handlers;


import config.Config;
import webserver.request.PostRequest;
import webserver.utils.HeaderUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handlers.parsers.RequestParser;
import webserver.request.HttpRequest;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private InputStream in;
    private Config config;

    public RequestHandler(InputStream in, Config config) {
        this.in = in;
        this.config = config;
    }

    public HttpRequest getHttpRequest() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, config.getEncoding()));

        String startLine = br.readLine();

        if (startLine == null) {
            throw new IllegalArgumentException();
            // null 값에 대한 처리는 어떻게..?
        }

        HttpRequest httpRequest = getRequest(startLine);

        Map<String, String> headers = getHeader(br);
        httpRequest.setHeaders(headers);

        setBody(httpRequest, headers, br);

        return httpRequest;
    }

    private HttpRequest getRequest(String startLine) {
        RequestParser requestParser = new RequestParser(startLine);
        return requestParser.extractRequest();
    }

    private void setBody(HttpRequest httpRequest, Map<String, String> headers, BufferedReader br) throws IOException {
        if (!(httpRequest instanceof PostRequest)) {
            return;
        }
        int length = Integer.parseInt(headers.get("Content-Length"));

        char[] body = new char[length];
        br.read(body, 0, length);
        httpRequest.setRequestBody(new String(body));
    }

    private Map<String, String> getHeader(BufferedReader br) throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();

        StringBuilder headerLine = new StringBuilder(br.readLine());
        while (!headerLine.toString().isEmpty()) {
            headers.put(HeaderUtils.getHeaderKey(headerLine.toString()), HeaderUtils.getHeaderValue(headerLine.toString()));
            headerLine.replace(0, headerLine.length(), "");
            headerLine.append(br.readLine());
        }

        return headers;
    }


}
