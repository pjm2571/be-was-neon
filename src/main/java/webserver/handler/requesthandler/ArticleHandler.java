package webserver.handler.requesthandler;

import webserver.StatusCode;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.sid.SidValidator;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ArticleHandler implements HttpRequestHandler {
    private static final String REDIRECT_URL = "/";
    private static final String LINE_SEPARATOR = "\n";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) {
        switch (httpRequest.getMethod()) {
            case GET -> {
                return handleGet(httpRequest);
            }
            case POST -> {
                return handlePost(httpRequest);
            }
            default -> {
                return HttpResponseUtils.get404Response();  // GET, POST 요쳥이 아닌 경우
            }
        }
    }

    private HttpResponse handleGet(HttpRequest httpRequest) {
        httpRequest = HttpRequestUtils.convertToStaticFileRequest(httpRequest);
        if (SidValidator.isLoggedIn(httpRequest)) {
            StaticFileHandler staticFileHandler = new StaticFileHandler();
            return staticFileHandler.handleRequest(httpRequest);
        }
        String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.FOUND);
        String header = HttpResponseUtils.generateRedirectResponseHeader(REDIRECT_URL);
        return new HttpResponse(startLine, header);
    }

    private HttpResponse handlePost(HttpRequest httpRequest) {
        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(httpRequest.getBody()));
        String boundary = getBoundary(httpRequest.getHeaderValue("Content-Type"));

        try {
            String line = readLine(reader);
            if (line.equals(boundary)) {
                StringJoiner sj = new StringJoiner(LINE_SEPARATOR);
                while (!(line = readLine(reader)).equals(boundary)) {
                    if (!line.isEmpty()) {
                        sj.add(line);
                    }
                }
                System.out.println(sj.toString());
            }

            // 이제 시작
            System.out.println(readLine(reader));
            System.out.println(readLine(reader));
            System.out.println(readLine(reader));

            // body를 읽자!

            System.out.println(readLine(reader));

        } catch (IOException e) {
            e.getMessage();
            return HttpResponseUtils.get500Response();
        }

        return HttpResponseUtils.get500Response();
    }

    private String getBoundary(String contentType) {
        String boundarySubstr = "boundary=";
        int boundaryIndex = contentType.indexOf(boundarySubstr);
        String boundary = contentType.substring(boundaryIndex + boundarySubstr.length());
        return "--" + boundary;
    }

    private String readLine(InputStreamReader reader) throws IOException {
        StringBuilder line = new StringBuilder();
        int character;
        while ((character = reader.read()) != -1) {
            if (character == '\n') {
                break;
            }
            if (character != '\r') {
                line.append((char) character);
            }
        }
        return line.toString();
    }


}
