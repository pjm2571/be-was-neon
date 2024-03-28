package webserver.handler.requesthandler;

import config.Config;
import db.Database;
import db.SessionStore;
import model.User;
import webserver.StatusCode;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;
import webserver.utils.SidUtils;

import java.io.*;
import java.util.StringJoiner;

public class UserListHandler implements HttpRequestHandler {
    private static final String ROOT_URL = "/";
    private static final String COOKIE = "Cookie";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) {
        switch (httpRequest.getMethod()) {
            case GET -> {
                return handleGet(httpRequest);
            }
            default -> {
                return HttpResponseUtils.get404Response();  // GET 요쳥이 아닌 경우
            }
        }
    }

    private HttpResponse handleGet(HttpRequest httpRequest) {
        httpRequest = HttpRequestUtils.convertToStaticFileRequest(httpRequest);
        if (isLoggedIn(httpRequest)) {
            return generateDynamicFileResponse(httpRequest);
        }
        String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.FOUND);
        String header = HttpResponseUtils.generateRedirectResponseHeader(ROOT_URL);
        return new HttpResponse(startLine, header);
    }

    private boolean isLoggedIn(HttpRequest httpRequest) {
        String cookie = httpRequest.getHeaderValue(COOKIE);
        try {
            String sid = SidUtils.getCookieSid(cookie);
            return isValidSid(sid);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValidSid(String sid) {
        return SessionStore.sessionIdExists(sid);
    }

    private HttpResponse generateDynamicFileResponse(HttpRequest httpRequest) {
        byte[] responseBody = getDynamicFileContent(httpRequest).getBytes();
        // 3) header 작성하기
        String header = HttpResponseUtils.generateStaticResponseHeader(httpRequest, responseBody.length);

        // 4) startLine 작성하기
        String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.OK);

        // 5) response 객체 리턴
        return new HttpResponse(startLine, header, responseBody);
    }

    private String getDynamicFileContent(HttpRequest httpRequest) {
        String staticContent = getStaticFileContent(httpRequest);

        String replacement = getAllUser();

        String pattern = "<ul(?:\\s+class=\"ul-user\"*\")?>[\\s\\S]*?</ul>";


        return staticContent.replaceFirst(pattern, replacement);
    }

    private String getAllUser() {
        StringJoiner joiner = new StringJoiner("\n");
        for (User user : Database.findAll()) {
            joiner.add("<li>" + user.getName() + "</li>");
        }
        return joiner.toString();
    }

    private String getStaticFileContent(HttpRequest httpRequest) {

        File file = new File(Config.getStaticRoute() + httpRequest.getRequestLine());

        // 404 error
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("[404 ERROR] Request 파일이 존재하지 않음 : " + file.getAbsolutePath());
        }

        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));) {
            String line;
            // 한 줄씩 읽어서 StringBuilder에 추가
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
            return contentBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException("[500 ERROR] 읽는 도중 문제 발생 ");
        }
    }
}
