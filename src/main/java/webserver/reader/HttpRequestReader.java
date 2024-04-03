package webserver.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/* 설정 관련 클래스 */
import config.Config;

/* 웹 서버의 HTTP 요청 객체 및 관련 클래스 */
import webserver.http.request.HttpRequest;
import webserver.http.request.message.HttpMethod;
import webserver.http.request.message.HttpRequestBody;
import webserver.http.request.message.HttpRequestHeader;
import webserver.http.request.message.HttpRequestStartLine;
import webserver.utils.HeaderUtils;

public class HttpRequestReader {
    private static final byte[] NONE = new byte[0];
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestReader.class);
    private static final char CR = '\r';
    private static final char LF = '\n';
    private static final int END_OF_LINE = -1;
    private static final int BUFFER_SIZE = 1024;

    private BufferedInputStream bis;

    public HttpRequestReader(InputStream in) {
        bis = new BufferedInputStream(in);
    }


    public HttpRequest getRequest() throws IOException {
        HttpRequestStartLine startLine = new HttpRequestStartLine(readLine());

        HttpRequestHeader header = new HttpRequestHeader(readHeader());

        HttpRequestBody body = new HttpRequestBody(readBody(startLine, header));
        return new HttpRequest(startLine, header, body);
    }

    private Map<String, String> readHeader() throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();

        StringBuilder headerLine = new StringBuilder(readLine());
        while (!headerLine.toString().isEmpty()) {
            headers.put(HeaderUtils.getHeaderKey(headerLine.toString()), HeaderUtils.getHeaderValue(headerLine.toString()));
            headerLine.replace(0, headerLine.length(), "");
            headerLine.append(readLine());
        }
        return headers;
    }

    private byte[] readBody(HttpRequestStartLine startLine, HttpRequestHeader header) throws IOException {
        if (startLine.getHttpMethod().equals(HttpMethod.GET)) {
            return NONE;
        }
        int contentLength = Integer.parseInt(header.getValue("Content-Length"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        int totalBytesRead = 0;

        while (totalBytesRead < contentLength &&
                (bytesRead = bis.read(buffer, 0, Math.min(BUFFER_SIZE, contentLength - totalBytesRead))) != END_OF_LINE) {
            baos.write(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;
        }
        return baos.toByteArray();
    }

    private String readLine() throws IOException {
        StringBuilder line = new StringBuilder();
        int character;
        while ((character = bis.read()) != END_OF_LINE) {
            if (character == LF) {
                break;
            }
            if (character != CR) {
                line.append((char) character);
            }
        }
        return line.toString();
    }


}
