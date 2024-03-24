package webserver.utils;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ContentType;
import webserver.StatusCode;
import webserver.handler.url.RootHandler;


import java.io.*;
import java.util.stream.Stream;

public class HttpResponseUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseUtils.class);

    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String SPACE = " ";
    private static final String CRLF = "\r\n";

    public static byte[] generateResponseBody(String httpRequestUrl, Config config) {
        File file = new File(config.getStaticRoute() + httpRequestUrl);

        if (!file.exists() || !file.isFile()) {
            logger.error("Requested file not found: {}", file.getAbsolutePath());
            return getNotFoundPage();
        }

        try (InputStream inputStream = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            logger.error("Failed to read file: {}", file.getAbsolutePath(), e);
            return getServerErrorPage();
        }
    }

    private static byte[] getNotFoundPage() {
        return "<h1>404 Not Found</h1>".getBytes(); // 404 오류 페이지 반환
    }

    private static byte[] getServerErrorPage() {
        return "<h1>500 Internal Server Error</h1>".getBytes(); // 서버 오류 페이지 반환
    }

    private static String getMimeType(String httpRequestUrl) {
        String extension = HttpRequestUtils.getExtension(httpRequestUrl);

        return Stream.of(ContentType.values())
                .filter(contentType -> contentType.name().equalsIgnoreCase(extension))
                .findFirst()
                .map(ContentType::getMimeType)
                .orElse(""); // 추가 처리 필요
    }

    public static String generateResponseStartLine(StatusCode statusCode) {
        return HTTP_VERSION + SPACE + statusCode.getCode() + SPACE + statusCode.getDescription() + CRLF;
    }

    public static String generateResponseHeader(int bodyLength, String httpRequestUrl) {
        String mimeType = getMimeType(httpRequestUrl);
        return "Content-Type:" + SPACE + mimeType + CRLF + "Content-Length:" + SPACE + bodyLength + CRLF + CRLF;
    }

    public static String generateResponseHeader(String redirectTarget) {
        return "Location:" + SPACE + redirectTarget + CRLF + CRLF;
    }


}
