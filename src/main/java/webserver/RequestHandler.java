package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final String STATIC_ROUTE = "src/main/resources/static";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));  // UTF-8로 HTTP Request 받아옴

            String requestLine = br.readLine();    // GET /index.html HTTP/1.1

            if (requestLine == null) {
                return;
            }

            logger.debug("request : {}", requestLine);  // HTTP Request의 첫번째 줄은 GET or POST 메소드

            String[] tokens = requestLine.split(" ");  // GET /index.html HTTP/1.1 을 공백 단위로 분리
            String path = tokens[1];   // 그 중, /index.html 을 가져온다

            StringBuilder headers = new StringBuilder(br.readLine());
            while (!headers.toString().isEmpty()) {
                logger.debug("header : {}", headers);
                headers.replace(0, headers.length(), "");
                headers.append(br.readLine());
            }

            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = Files.readAllBytes(new File(STATIC_ROUTE + path).toPath()); // path 설정

            response200Header(dos, body.length);
            responseBody(dos, body);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
