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
