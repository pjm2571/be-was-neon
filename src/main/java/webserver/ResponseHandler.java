package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.StringUtils;

import java.io.*;

import static webserver.ContentType.getContentType;

public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    private DataOutputStream dos;
    private byte[] body;
    private String path;

    ResponseHandler(OutputStream out, String path) {
        this.dos = new DataOutputStream(out);
        this.path = path;
    }

    public void response200Header() {
        this.body = readBody(Route.STATIC.getRoute(path));
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes(getContentType(StringUtils.getType(path)));  // path 경로에서 .ivo, .svg, .html 등 확장자 명을 가져와서 content-type을 설정한다.
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void response302Header() {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location : /index.html\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        
    }

    private byte[] readBody(String path) {
        File file = new File(path);
        byte[] body = new byte[(int) file.length()];

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            bis.read(body); // 읽어온 바이트 수를 리턴하므로 리턴값을 처리하지 않음
        } catch (IOException e) {
            e.printStackTrace(); // 예외 처리
        }
        return body;
    }

    public void responseBody() {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
