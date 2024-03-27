package webserver.handler.file;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ContentType;
import webserver.StatusCode;
import webserver.handler.error.ErrorHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;

import java.util.Arrays;

public class StaticFileHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticFileHandler.class);

    public HttpResponse handleRequest(HttpRequest httpRequest, Config config) {
        try {
            // 1) 지원하는 파일인지 체크! -> 없다면 IllegalArgumentException [404]
            validateMimeType(httpRequest.getRequestLine());

            // 2) 파일 읽기 -> 없다면 IllegalArgumentException [404]
            //            -> 읽다가 예외 발생 시 RuntimeException [500]
            byte[] responseBody = HttpResponseUtils.generateStaticResponseBody(httpRequest, config);

            // 3) header 작성하기
            String header = HttpResponseUtils.generateStaticResponseHeader(httpRequest, responseBody.length);

            // 4) startLine 작성하기
            String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.OK);

            // 5) response 객체 리턴
            return new HttpResponse(startLine, header, responseBody);

        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return ErrorHandler.get404Response(); // 파일을 못찾으면 404 처리
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return ErrorHandler.get500Response(); // 서버 오류 500 처리
        }
    }

    private void validateMimeType(String requestLine) {
        String extension = HttpRequestUtils.getExtension(requestLine);

        if (!isValidExtension(extension)) {
            throw new IllegalArgumentException("[ERROR] 지원하지 않는 Mime Type : " + extension);
        }
    }

    private boolean isValidExtension(String extension) {
        return Arrays.stream(ContentType.values())
                .anyMatch(contentType -> contentType.name().equals(extension)); // 정의된 확장자 중에 있을 때는 true!
    }


}
