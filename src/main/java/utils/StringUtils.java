package utils;

import webserver.ContentType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StringUtils {
    private StringUtils() {
    }

    public static final String SPACE = " ";
    public static final String DOT = "\\.";
    public static final String AND = "&";
    public static final String EQUAL = "=";
    public static final String DEFAULT_FILE = "/index.html";
    public static final String QUERY_SEQUENCE = "\\?";

    /* -------------------------------------- Request Line 파싱하는 메소드 ------------------------------- */

    /* 1) GET or POST 와 같은 메소드를 추출하는 메소드*/
    public static String getHttpMethod(String requestLine) {
        String[] tokens = requestLine.split(SPACE);
        return tokens[0];
    }

    /* /create, /index.html 와 같은 requestTarget을 추출하는 메소드 */
    public static String getRequestTarget(String requestLine) {
        String[] tokens = requestLine.split(SPACE);
        String requestTarget = tokens[1];
        if (isURL(requestTarget)) {
            return requestTarget + DEFAULT_FILE;
        }
        return requestTarget;
    }

    // RequestTarget이 URL인지 확인하는 함수
    private static boolean isURL(String requestTarget) {
        if (isQueryType(requestTarget)) { // GET 쿼리라면, false
            return false;
        }

        if (isContentType(requestTarget)) {
            return false;
        }

        return true;
    }

    private static boolean isQueryType(String requestTarget) {
        CharSequence querySequence = String.valueOf(QUERY_SEQUENCE.charAt(1));
        return requestTarget.contains(querySequence);
    }

    private static boolean isContentType(String requestTarget) {
        return Arrays.stream(ContentType.values())
                .anyMatch(contentType -> contentType.name().equals(getType(requestTarget)));
    }


    /* HTTP/1.1 와 같은 http 버전을 추출하는 메소드 */
    public static String getHttpVersion(String requestLine) {
        String[] tokens = requestLine.split(SPACE);
        return tokens[2];
    }

    /* ---------------------------------------------- END ---------------------------------------------- */



    /* -------------------------------------- Request Target 파싱하는 메소드 ------------------------------- */


    /* requestTarget에서 url을 추출하는 메소드 */
    public static String getRequestUrl(String requestTarget) {
        String[] tokens = requestTarget.split(QUERY_SEQUENCE);
        return tokens[0];
    }

    /* requestTarget에서 parameter들을 가져오는 메소드 */
    public static String[] getRequestParams(String requestTarget) {
        String[] tokens = requestTarget.split(QUERY_SEQUENCE);

        String params = tokens[1];

        return params.split(AND);
    }

    /* parameter들의 집합에서, key : value 쌍을 추출하는 메소드 */
    public static Map<String, String> getParams(String requestTarget) {
        /* name : zoonmy
         email : zoonmy@lucas.com
         을 담기위한 map 생성
         Key : name, Value : value */
        Map<String, String> params = new HashMap<>();

        String[] paramComponents = getRequestParams(requestTarget);

        for (int i = 0; i < paramComponents.length; i++) {
            int subIndex = paramComponents[i].indexOf(EQUAL);
            params.put(paramComponents[i].substring(0, subIndex), paramComponents[i].substring(subIndex + 1));
        }

        return params;
    }

    public static String getType(String requestTarget) {
        String[] tokens = requestTarget.split(DOT); // .을 기준으로 자른다.
        return tokens[tokens.length - 1];           // 맨 마지막 값을 가져온다. 그것이 type!
    }


    /* ---------------------------------------------- END ---------------------------------------------- */


}
