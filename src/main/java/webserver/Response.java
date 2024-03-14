package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.StringUtils;

import java.io.*;

import static webserver.ContentType.getContentType;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    private final static String STATIC = "src/main/resources/static";
    private static final String CREATE = "/create";

    private Request request;
    private String responseHeader;
    private byte[] responseBody;

    Response(Request request) {
        this.request = request;
        setResponse();
    }

    private void setResponse() {
        if (request.getRequestTarget().startsWith(CREATE)) {
            responseHeader = set302responseHeader();
            responseBody = null;

            return;
        }
        responseBody = readBody(request.getRequestTarget());
        responseHeader = set200responseHeader();
    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    private String set302responseHeader() {
        return "HTTP/1.1 302 Found \r\n" +
                "Location : " + "/index.html" + "\r\n" +
                "\r\n";
    }

    private String set200responseHeader() {
        return "HTTP/1.1 200 OK \r\n" +
                getContentType(StringUtils.getType(request.getRequestTarget())) +
                "Content-Length: " + responseBody.length + "\r\n" +
                "\r\n";
    }

    private byte[] readBody(String requestTarget) {
        File file = new File(STATIC + requestTarget);
        byte[] body = new byte[(int) file.length()];

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            bis.read(body); // 읽어온 바이트 수를 리턴하므로 리턴값을 처리하지 않음
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리
        }
        return body;
    }

//    public void responseBody() {
//        try {
//            dos.write(body, 0, body.length);
//            dos.flush();
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
}
