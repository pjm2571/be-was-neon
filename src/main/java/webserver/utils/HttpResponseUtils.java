package webserver.utils;

import config.Config;
import webserver.ContentType;
import webserver.StatusCode;
import webserver.request.HttpRequest;

import java.io.*;

import static webserver.constants.Constants.*;

public class HttpResponseUtils {

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
}
