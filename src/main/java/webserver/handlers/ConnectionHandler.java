package webserver.handlers;

import java.io.IOException;
import java.net.Socket;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handlers.request.HttpRequest;
import webserver.handlers.request.QueryRequest;

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
        try {
            RequestHandler requestHandler = new RequestHandler(connection.getInputStream(), config);

            HttpRequest httpRequest = requestHandler.getHttpRequest();

            ResponseHandler responseHandler = new ResponseHandler(connection.getOutputStream(), httpRequest.sendResponse(), config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
