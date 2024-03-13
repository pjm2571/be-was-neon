package webserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static webserver.ContentType.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.StringUtils;


public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String ENCODING = "UTF-8";
    private static final String CREATE = "create";
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            BufferedReader br = new BufferedReader(new InputStreamReader(in, ENCODING));  // UTF-8로 HTTP Request 받아옴

            String requestLine = br.readLine();

            Request request = new Request(requestLine, getRequestHeaders(br));

            if (requestLine.contains(CREATE)) {
                CreateHandler createHandler = new CreateHandler(request.getRequestTarget());
                createHandler.create();

                ResponseHandler responseHandler = new ResponseHandler(out, request.getRequestTarget());
                responseHandler.response302Header();

                return;
            }


            ResponseHandler responseHandler = new ResponseHandler(out, request.getRequestTarget());

            responseHandler.response200Header();
            responseHandler.responseBody();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private ArrayList<String> getRequestHeaders(BufferedReader br) throws IOException {
        ArrayList<String> params = new ArrayList<>();

        StringBuilder headers = new StringBuilder(br.readLine());   // String 재할당을 쓰지 않기 위해 StringBuilder 사용

        while (!headers.toString().isEmpty()) {
            params.add(headers.toString());
            headers.replace(0, headers.length(), "");     // StringBuilder의 모든 내용을 ""으로 초기화 -> new StringBuilder보다 낫다.
            headers.append(br.readLine());  // 다음 값 매핑
        }

        return params;
    }


}
