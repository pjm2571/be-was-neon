package webserver.handlers.readers;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HeaderUtils;
import webserver.WebServer;
import webserver.handlers.request.HttpRequest;
import webserver.handlers.parsers.RequestParser;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestReader {
    private static final Logger logger = LoggerFactory.getLogger(RequestReader.class);
    private BufferedReader requestReader;

    public RequestReader(InputStream inputStream, Config config) {
        try {
            this.requestReader = new BufferedReader(new InputStreamReader(inputStream, config.getEncoding()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpRequest getHttpRequest() throws IOException {
        // 1) 첫 줄을 읽어서, Request Parser를 생성한다.
        RequestParser requestParser = new RequestParser(requestReader.readLine());

        // Request Parser가 requestTarget을 분석하여 어떤 타입인지 리턴해준다.
        HttpRequest httpRequest = requestParser.extractRequest();

        // 2) requestHeader을 읽는다.
        // 읽은 Header를 Request 객체에 넣어준다.
        httpRequest.setHeaders(getHeader(requestReader));

        // 3) requestBody를 읽는다.
        // .. POST 일때만 이므로, 아직은 미구현!

        return httpRequest;
    }

    private Map<String, String> getHeader(BufferedReader requestReader) throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();

        StringBuilder headerLine = new StringBuilder(requestReader.readLine());
        while (!headerLine.toString().isEmpty()) {
            logger.debug("Request Header : {} : {}", HeaderUtils.getHeaderKey(headerLine.toString()), HeaderUtils.getHeaderValue(headerLine.toString()));
            headers.put(HeaderUtils.getHeaderKey(headerLine.toString()), HeaderUtils.getHeaderValue(headerLine.toString()));
            headerLine.replace(0, headerLine.length(), "");
            headerLine.append(requestReader.readLine());
        }

        return headers;
    }


}
