package webserver.utils;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import webserver.type.ContentType;

import java.util.Arrays;

public class RequestUtils {
    private final static String POST_METHOD = "POST";
    private final static String SPACE = " ";
    private final static String DOT = "\\.";

    private RequestUtils() {
    }

    /* ---------------- startLine 파싱하는 기능 모음 ---------------- */
    public static String getHttpMethod(String startLine) {
        String[] tokens = startLine.split(SPACE);
        return tokens[0];
    }

    public static String getRequestTarget(String startLine) {
        String[] tokens = startLine.split(SPACE);
        return tokens[1];
    }

    public static String getHttpVersion(String startLine) {
        String[] tokens = startLine.split(SPACE);
        return tokens[2];
    }

    public static boolean isPostRequest(String startLine) {
        return getHttpMethod(startLine).equals(POST_METHOD);
    }

    /* ---------------------------------------------------------- */

    /* ---------------- request Target 파싱하는 기능 모음 ---------------- */


    public static boolean isQueryRequest(String requestTarget) {
        return requestTarget.contains("?");
    }

    public static boolean isStaticFile(String requestTarget) {
        String extension = getExtension(requestTarget);

        return Arrays.stream(ContentType.values())
                .anyMatch(contentType -> contentType.name().equals(extension));
    }

    public static boolean isUrl(String requestTarget) {
        return !isStaticFile(requestTarget) && !isQueryRequest(requestTarget);
    }

    public static String getExtension(String requestTarget) {
        String[] tokens = requestTarget.split(DOT);
        return tokens[tokens.length - 1];
    }

    /* ---------------------------------------------------------- */


}
