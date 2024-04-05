package webserver.handler.requesthandler;

import db.H2.ArticleDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.handler.HttpRequestHandler;
import model.Article;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.sid.SidValidator;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticleHandler implements HttpRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(ArticleHandler.class);
    private static final String REDIRECT_URL = "/";

    private static final String BOUNDARY_REGEX = "^------WebKitFormBoundary.*$";
    private static final Pattern CONTENT_PATTERN
            = Pattern.compile("^ *Content-Disposition: *form-data; *name=\"content\"");
    private static final Pattern IMAGE_PATTERN
            = Pattern.compile("^ *Content-Disposition: *form-data; *name=\"image\"; *filename=\"(.*?)\"");
    private static final String DIRECTORY_PATH = "./src/main/resources/static/db/";
    private static final String IMG_PATH = "./db/";

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
        String userId = SidValidator.getUserId(httpRequest);
        Article article = generateArticle(httpRequest.getBody(), userId);
        logger.debug("article : {}", article);
        ArticleDatabase.addArticle(article);

        String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.FOUND);
        String header = HttpResponseUtils.generateRedirectResponseHeader("/");
        return new HttpResponse(startLine, header);
//        return HttpResponseUtils.get500Response();
    }

    private Article generateArticle(byte[] requestBody, String userId) {
        String content = "";
        String imageName = "";

        int start = 0;

        while (start < requestBody.length) {    // 모든 request Body를 읽을 때 까지 동작
            int end = getEndOfByteLine(start, requestBody); // end 포인트를 찾는다 '\n' 까지 찾는다
            String convertedStr = convertByteToStr(requestBody, start, end); // 한 줄을 String으로 변환한다.
            start = end + 1;    // 다음 줄을 읽도록 start Index를 옮긴다

            if (convertedStr.matches(BOUNDARY_REGEX)) { // 변환한 String이 Boundary Regex와 일치한다면
                end = getEndOfByteLine(start, requestBody); // 옮긴 시작점부터 시작해서 \n이 나타날 때 까지의 index를 가져온다.
                String contentInfo = convertByteToStr(requestBody, start, end); // contentInfo = start부터 개행문자 전까지의 값을 가져온다
                start = end + 1;    // CRLF 를 넘긴다

                Matcher contentMatcher = CONTENT_PATTERN.matcher(contentInfo);
                if (contentMatcher.find()) {    // Content-Pattern과 일치한다면
                    start += 2; // content-skip (1) ,  CRLF-skip (2) 이므로 +2를 해준다.
                    end = getEndOfContent(start, requestBody);
                    content = convertByteToStr(requestBody, start, end);
                }

                Matcher imageMatcher = IMAGE_PATTERN.matcher(contentInfo);
                if (imageMatcher.find()) {
                    start += 2;
                    end = getEndOfByteLine(start, requestBody);

                    imageName = decodeStr(imageMatcher.group(1));
                    byte[] image = Arrays.copyOfRange(requestBody, end + 3, requestBody.length - 2);
                    generateFile(imageName, image);
                    break;
                }
            }
        }
        String filePath = IMG_PATH + imageName;
        return new Article(userId, content, filePath);
    }


    private int getEndOfContent(int start, byte[] requestBody) {
        int lineEnd = getEndOfByteLine(start, requestBody);
        int contentEnd = lineEnd;

        String line = convertByteToStr(requestBody, start, lineEnd);
        while (!line.matches(BOUNDARY_REGEX)) {
            contentEnd = lineEnd;
            start = lineEnd + 1;
            lineEnd = getEndOfByteLine(start, requestBody);
            line = convertByteToStr(requestBody, start, lineEnd);
        }
        return contentEnd;
    }


    private void generateFile(String imageName, byte[] image) {
        String filePath = DIRECTORY_PATH + imageName; // 파일 경로 및 이름을 지정

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(image);
            logger.debug("이미지 파일이 성공적으로 생성되었습니다.");
        } catch (IOException e) {
            logger.error("파일 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private String decodeStr(String matches) {
        return URLDecoder.decode(matches, StandardCharsets.UTF_8);
    }

    private String convertByteToStr(byte[] requestBody, int start, int end) {
        byte[] copyByte = Arrays.copyOfRange(requestBody, start, end - 1);
        return new String(copyByte, StandardCharsets.UTF_8);
    }

    private int getEndOfByteLine(int start, byte[] requestBody) {
        int end = requestBody.length;
        for (int index = start; index < requestBody.length; index++) {
            if (requestBody[index] == '\n') {
                end = index;
                break;
            }
        }
        return end;
    }

}
