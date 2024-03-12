package webserver;

import java.io.*;
import java.net.Socket;

import static webserver.ContentType.*;
import static webserver.Route.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.StringUtils;


public class RequestHandler implements Runnable {
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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));  // UTF-8로 HTTP Request 받아옴

            String requestLine = br.readLine();    // GET /index.html HTTP/1.1

            if (requestLine == null) return;    // http request line이 null 이라면 스레드 종료

            logger.debug("request : {}", requestLine);

            // GET /create?userId=z&name=a&password=b&email=pj%40nave.com HTTP/1.1
            if (requestLine.contains(CREATE)) {
                CreateHandler createHandler = new CreateHandler(requestLine);
                createHandler.create();
                return;
            }

            String path = StringUtils.getPath(requestLine);

            StringBuilder headers = new StringBuilder(br.readLine());   // String 재할당을 쓰지 않기 위해 StringBuilder 사용
            while (!headers.toString().isEmpty()) {
                logger.debug("header : {}", headers.toString());
                headers.replace(0, headers.length(), "");     // StringBuilder의 모든 내용을 ""으로 초기화 -> new StringBuilder보다 낫다.
                headers.append(br.readLine());  // 다음 값 매핑
            }

            DataOutputStream dos = new DataOutputStream(out);   // 데이터 처리를 위한 DataOutputStream 생성

            byte[] body = readBody(STATIC.getRoute(path));

            response200Header(dos, body.length, path);  // output header 작성

            responseBody(dos, body);    // output body 작성

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
