package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static webserver.ContentType.*;
import static webserver.Route.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.StringUtils;


public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String REQUEST = "request";
    private static final String HEADER = "header";
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

            if (requestLine == null) return;    // http request line이 null 이라면 스레드 종료

            loggerDebug(REQUEST, requestLine);  // request 타입에 대한 log.debug

            String path = StringUtils.getPath(requestLine);

            StringBuilder headers = new StringBuilder(br.readLine());   // String 재할당을 쓰지 않기 위해 StringBuilder 사용
            while (!headers.toString().isEmpty()) {
                loggerDebug(HEADER, headers.toString());
                headers.replace(0, headers.length(), "");     // StringBuilder의 모든 내용을 ""으로 초기화 -> new StringBuilder보다 낫다.
                headers.append(br.readLine());  // 다음 값 매핑
            }

            DataOutputStream dos = new DataOutputStream(out);   // 데이터 처리를 위한 DataOutputStream 생성

            byte[] body = Files.readAllBytes(new File(STATIC.getRoute(path)).toPath()); // static 값만 매핑
            // 추후에, template이 나오면 template으로 변경 가능성 있음

            response200Header(dos, body.length, path);  // output header 작성

            responseBody(dos, body);    // output body 작성

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    /*
     * requestMessage : request or header 임을 알려주는 메시지
     * requestLine : httpRequestLine
     * -> logger.debug 메소드를 활용해서 입력받은 requestLine을 디버그해준다.
     */
    private void loggerDebug(String requestMessage, String requestLine) {
        logger.debug(requestMessage + " : {}", requestLine);
    }


    /* http response 200 : 성공적으로 http request가 수행되었다는 메세지*/
    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String path) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes(getContentType(StringUtils.getType(path)));  // path 경로에서 .ivo, .svg, .html 등 확장자 명을 가져와서 content-type을 설정한다.
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
