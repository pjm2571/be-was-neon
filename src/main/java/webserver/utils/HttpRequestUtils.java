package webserver.utils;


import config.Config;
import webserver.ContentType;
import webserver.StatusCode;
import webserver.Url;
import webserver.request.HttpRequest;
import webserver.request.message.HttpRequestStartLine;

import java.io.*;
import java.util.Arrays;

import static webserver.constants.Constants.*;

public class HttpRequestUtils {


    /* ---------------- Request Line 파싱하는 기능 모음 ---------------- */

    public static boolean isStaticFile(String requestLine) {
        return requestLine.contains(".");
    }

    public static String getExtension(String requestLine) {
        return requestLine.substring(requestLine.lastIndexOf(".") + 1);
    }

    public static String getMimeType(String extension) {
        return Arrays.stream(ContentType.values())
                .filter(type -> type.name().equalsIgnoreCase(extension))
                .findFirst()
                .map(ContentType::getMimeType)
                .orElse("application/octet-stream"); // 못읽을 시, 기본값 설정
    }

    // --

    public static byte[] generateStaticResponseBody(HttpRequest httpRequest, Config config) {
        File file = new File(config.getStaticRoute() + httpRequest.getRequestLine());

        // 404 error
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("[404 ERROR] Request 파일이 존재하지 않음 : " + file.getAbsolutePath());
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
        } catch (IOException e) {   // 500 error
            throw new RuntimeException("[500 ERROR] 파일을 읽는 중, 서버 오류 발생");
        }
    }

    public static String generateStaticResponseHeader(HttpRequest httpRequest, int bodyLength) {
        String extension = HttpRequestUtils.getExtension(httpRequest.getRequestLine());

        String mimeType = HttpRequestUtils.getMimeType(extension);

        return "Content-Type:" + SPACE + mimeType + CRLF + "Content-Length:" + SPACE + bodyLength + CRLF + CRLF;
    }

    public static String generateErrorResponseHeader(int bodyLength) {
        return "Content-Type:" + SPACE + ContentType.html.getMimeType() + CRLF + "Content-Length:" + SPACE + bodyLength + CRLF + CRLF;
    }

    public static String generateRedirectResponseHeader(String redirectTarget) {
        return "Location:" + SPACE + redirectTarget + CRLF + CRLF;
    }

    public static String setCookieHeader(String header, String sid) {
        int length = CRLF.length();
        header = header.substring(0, header.length() - length);
        return header + "Set-Cookie:" + SPACE + "sid=" + sid + ";" + SPACE + "Path=/" + CRLF + CRLF;
    }


    public static String generateResponseStartLine(StatusCode statusCode) {
        return HTTP_VERSION + SPACE + statusCode.getCode() + SPACE + statusCode.getDescription() + CRLF;
    }

    // --

    public static boolean isRootUrl(String requestLine) {
        return requestLine.equals(ROOT_URL);
    }

    public static Url getUrl(String requestLine) {
        return Arrays.stream(Url.values())
                .filter(url -> requestLine.startsWith(url.getUrlPath()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[404 ERROR] 해당하는 URL이 없습니다."));
    }

    public static HttpRequest convertToStaticFileRequest(HttpRequest httpRequest) {
        String staticRequestLine = httpRequest.getRequestLine() + SLASH + DEFAULT_FILE;
        HttpRequestStartLine startLine = new HttpRequestStartLine(httpRequest.getMethod() + SPACE + staticRequestLine + SPACE + httpRequest.getHttpVersion());
        return new HttpRequest(startLine, httpRequest.getRequestHeader(), httpRequest.getRequestBody());
    }

    /* ---------------------------------------------------------- */


}
