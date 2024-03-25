package webserver.utils;


import config.Config;
import webserver.ContentType;
import webserver.StatusCode;
import webserver.Url;
import webserver.request.HttpRequest;

import java.io.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static webserver.constants.Constants.*;

public class HttpRequestUtils {
    private static final String ROOT_DIRECTORY = "/";
    private static final String DEFAULT_FILE = "/index.html";
    private static final String URL_SEPARATOR = "/";

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
            throw new IllegalArgumentException("[ERROR] Request 파일이 존재하지 않음 : " + file.getAbsolutePath());
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
            throw new RuntimeException("[ERROR] 파일을 읽는 중, 서버 오류 발생");
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

    public static String generateResponseStartLine(StatusCode statusCode) {
        return HTTP_VERSION + SPACE + statusCode.getCode() + SPACE + statusCode.getDescription() + CRLF;
    }


    // --
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




    /* ---------------------------------------------------------- */


}
