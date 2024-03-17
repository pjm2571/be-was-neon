package webserver.handlers;

import java.io.IOException;
import java.net.Socket;

import config.Config;
import webserver.handlers.request.HttpRequest;

public class ConnectionHandler implements Runnable {

    private Socket connection;
    private Config config;

    public ConnectionHandler(Socket connection, Config config) {
        this.connection = connection;
        this.config = config;
    }


    @Override
    public void run() {
        // Request Handler 생성
        try {
            RequestHandler requestHandler = new RequestHandler(connection.getInputStream(), config);
            HttpRequest httpRequest = requestHandler.getHttpRequest();

//            ResponseHandler responseHandler = new ResponseHandler(connection.getOutputStream(), config);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
