package webserver.handler.requesthandler;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.utils.HttpResponseUtils;

import java.io.*;

public class DynamicFileHandler implements HttpRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(DynamicFileHandler.class);

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) {
        try {
            return generateDynamicFileResponse(httpRequest);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return HttpResponseUtils.get404Response(); // 파일을 못찾으면 404 처리
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return HttpResponseUtils.get500Response(); // 서버 오류 500 처리
        }
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

        String replacement = "<ul class=\"header__menu\">\n" +
                "    <li class=\"header__menu__item\">\n" +
                "        <a class=\"btn btn_contained btn_size_s\" href=\"/article\">글쓰기</a>\n" +
                "    </li>\n" +
                "    <li class=\"header__menu__item\">\n" +
                "        <button id=\"logout-btn\" class=\"btn btn_ghost btn_size_s\" onclick=\"window.location.href='/logout'\">로그아웃</button>\n" +
                "    </li>\n" +
                "</ul>";

        String pattern = "<ul(?:\\s+class=\"[^\"]*\")?>[\\s\\S]*?</ul>";

        return staticContent.replaceFirst(pattern, replacement);
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
