package webserver.handler;

import java.io.*;
import java.net.Socket;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.reader.HttpRequestReader;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class ConnectionHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);

    private Socket connection;
    private Config config;

    public ConnectionHandler(Socket connection, Config config) {
        this.connection = connection;
        this.config = config;
    }


    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());


        // Request Handler 생성
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequestReader requestReader = new HttpRequestReader(in, config);    // InputStream의 값을 모두 읽음

            HttpRequest httpRequest = requestReader.getRequest();   // 읽은 후, HttpRequest 객체를 생성함
            HttpRequestHandler requestHandler = new HttpRequestHandler(httpRequest);

            HttpResponse httpResponse = requestHandler.handleRequest(config);
            HttpResponseHandler httpResponseHandler = new HttpResponseHandler(out);

            httpResponseHandler.handleResponse(httpResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            return;
        }
    }
}
