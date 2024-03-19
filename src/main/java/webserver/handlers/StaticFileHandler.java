package webserver.handlers;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RequestUtils;
import webserver.handlers.response.HttpResponse;
import webserver.types.ContentType;

import java.io.*;
import java.util.stream.Stream;

public class StaticFileHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticFileHandler.class);

    private DataOutputStream dos;
    private HttpResponse httpResponse;
    private Config config;

    public StaticFileHandler(DataOutputStream dos, HttpResponse httpResponse, Config config) {
        this.dos = dos;
        this.httpResponse = httpResponse;
        this.config = config;
    }

    public void handleResponse() {
        setResponse();
        try {
            dos.writeBytes(httpResponse.getStartLine());
            dos.writeBytes(httpResponse.getResponseHeader());
            dos.write(httpResponse.getResponseBody(), 0, httpResponse.getResponseBody().length);

            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void setResponse() {
        byte[] body = getBody();
        httpResponse.setResponseBody(body);
        String mimeType = getMimeType(httpResponse.getRequestTarget());
        httpResponse.setResponseHeader(mimeType, body.length);
        httpResponse.setStartLine(StatusCode.OK);
    }

    private String getMimeType(String requestTarget) {
        String extension = RequestUtils.getExtension(requestTarget);

        return Stream.of(ContentType.values())
                .filter(contentType -> contentType.name().equalsIgnoreCase(extension))
                .findFirst()
                .map(ContentType::getMimeType)
                .orElse("");
    }

    private byte[] getBody() {
        File file = new File(config.getStaticRoute() + httpResponse.getRequestTarget());

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

    private byte[] getNotFoundPage() {
        return "<h1>404 Not Found</h1>".getBytes(); // 404 오류 페이지 반환
    }

    private byte[] getServerErrorPage() {
        return "<h1>500 Internal Server Error</h1>".getBytes(); // 서버 오류 페이지 반환
    }
}
