package webserver.utils;


import webserver.ContentType;
import webserver.Url;
import webserver.request.message.HttpRequestStartLine;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestUtils {
    private static final String DOT = "\\.";
    private static final String ROOT_DIRECTORY = "/";
    private static final String DEFAULT_FILE = "/index.html";
    private static final String URL_SEPARATOR = "/";

    private static final String URL_FILENAME_PATTERN = "\\/+[a-z]*.([a-z]*)"; // url만 걸러주는 필터


    /* ---------------- Request Line 파싱하는 기능 모음 ---------------- */

    public static Url getUrl(String requestLine) {
        String urlPath = getUrlPath(requestLine);

        Optional<Url> matchingPath = Arrays.stream(Url.values())
                .filter(url -> urlPath.equals(url.getUrlPath()))
                .findFirst();

        // 1) url에 존재하는 경우에는 url을 리턴해준다.
        if (matchingPath.isPresent()) {
            return matchingPath.get();
        }


        // url에서 찾을 수 없는 경우 exception 발생
        throw new IllegalArgumentException("[ERROR] 등록된 Url이 없습니다.");

    }

    private static String getUrlPath(String requestLine) {
        if (isRootDirectory(requestLine)) { // Root Directory일 경우
            return ROOT_DIRECTORY;
        }
        if (isStaticFile(requestLine)) {    // Root Directory의 static file 조회하는 경우
            return ROOT_DIRECTORY;
        }
        // url이 루트가 아닐 경우는 모두 url을 리턴하도록 구현한다.
        return requestLine.split(URL_SEPARATOR)[1]; // static file & root 디렉토리도 아닌 경우
    }

    private static boolean isRootDirectory(String requestLine) {
        return requestLine.equals(ROOT_DIRECTORY);
    }


    public static boolean isUrl(String requestLine) {
        return !requestLine.contains(".");
    }

    public static String urlToStaticFile(String requestLine) {
        StringBuilder sb = new StringBuilder(requestLine);

        if (requestLine.endsWith(URL_SEPARATOR)) {  /* /로 끝난다면, /를 지우고 default file을 추가 */
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(DEFAULT_FILE);

        return sb.toString();
    }

    public static boolean isStaticFile(String requestLine) {
        String extension = getExtension(requestLine);   // . 이후의 확장자 파일명을 가져온다.

        return Arrays.stream(ContentType.values())
                .anyMatch(contentType -> contentType.name().equals(extension)); // 정의된 확장자 중에 있을 때는 true!
    }


    public static String getExtension(String requestLine) {
        return requestLine.substring(requestLine.lastIndexOf(".") + 1);
    }

    /* ---------------------------------------------------------- */


}
