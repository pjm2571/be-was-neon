package webserver.handler;

import java.io.*;
import java.net.Socket;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.router.RequestHandlerRouter;
import webserver.router.ResponseHandlerRouter;
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

            RequestHandlerRouter requestHandlerRouter = new RequestHandlerRouter(in, config);

            RequestHandler requestHandler = requestHandlerRouter.getRequestHandler();

            HttpRequest httpRequest = requestHandler.getHttpRequest();


            ResponseHandlerRouter responseHandlerRouter = new ResponseHandlerRouter(out, config, httpRequest);

            ResponseHandler responseHandler = responseHandlerRouter.getResponseHandler();

            responseHandler.handleResponse();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            return;
        }
    }
}
