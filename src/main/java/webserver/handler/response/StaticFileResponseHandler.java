package webserver.handler.response;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.StaticFileResponse;
import webserver.StatusCode;
import webserver.ContentType;
import webserver.utils.RequestUtils;

import java.io.*;
import java.util.stream.Stream;

public class StaticFileResponseHandler extends ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticFileResponseHandler.class);

    public StaticFileResponseHandler(DataOutputStream responseWriter, Config config, HttpRequest httpRequest) {
        super(responseWriter, config, httpRequest);
    }

    @Override
    public void handleResponse() {
        // 1) responseBody 읽기
        byte[] responseBody = generateResponseBody();

        // 2) 읽은 후, 성공 표시 -> try-catch 개선 필요함!
        String responseStartLine = generateResponseStartLine(StatusCode.OK);

        // 3) body 정보와 mime 정보를 이용하여 header 구성
        String mimeType = getMimeType(httpRequest.getRequestTarget());
        String responseHeader = generateResponseHeader(responseBody.length, mimeType);

        HttpResponse httpResponse = new StaticFileResponse(responseStartLine, responseHeader, responseBody);

        writeResponse(httpResponse);
    }

    private void writeResponse(HttpResponse httpResponse) {
        try {
            responseWriter.writeBytes(httpResponse.getStartLine());
            responseWriter.writeBytes(httpResponse.getRequestHeader());
            responseWriter.write(httpResponse.getRequestBody(), 0, httpResponse.getRequestBody().length);

            responseWriter.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String generateResponseHeader(int bodyLength, String mimeType) {
        return "Content-Type:" + SPACE + mimeType + CRLF + "Content-Length:" + SPACE + bodyLength + CRLF + CRLF;
    }

    private byte[] generateResponseBody() {
        File file = new File(config.getStaticRoute() + httpRequest.getRequestTarget());

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

    private String getMimeType(String requestTarget) {
        String extension = RequestUtils.getExtension(requestTarget);

        return Stream.of(ContentType.values())
                .filter(contentType -> contentType.name().equalsIgnoreCase(extension))
                .findFirst()
                .map(ContentType::getMimeType)
                .orElse(""); // 추가 처리 필요
    }

    private byte[] getNotFoundPage() {
        return "<h1>404 Not Found</h1>".getBytes(); // 404 오류 페이지 반환
    }

    private byte[] getServerErrorPage() {
        return "<h1>500 Internal Server Error</h1>".getBytes(); // 서버 오류 페이지 반환
    }

}
