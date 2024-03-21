package webserver.handler;

import java.io.*;
import java.net.Socket;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.parser.RequestHandlerParser;
import webserver.parser.ResponseHandlerParser;
import webserver.handler.request.RequestHandler;
import webserver.handler.response.ResponseHandler;
import webserver.request.HttpRequest;

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

            RequestHandlerParser requestHandlerParser = new RequestHandlerParser(in, config);

            RequestHandler requestHandler = requestHandlerParser.getRequestHandler();

            HttpRequest httpRequest = requestHandler.getHttpRequest();


            ResponseHandlerParser responseHandlerParser = new ResponseHandlerParser(out, config, httpRequest);

            ResponseHandler responseHandler = responseHandlerParser.getResponseHandler();

            responseHandler.handleResponse();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            return;
        }
    }
}
