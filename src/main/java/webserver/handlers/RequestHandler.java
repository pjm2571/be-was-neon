package webserver.handlers;


import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServer;
import webserver.handlers.request.HttpRequest;
import webserver.handlers.readers.RequestReader;

import java.io.IOException;
import java.io.InputStream;

public class RequestHandler {

    private RequestReader requestReader;

    public RequestHandler(InputStream inputStream, Config config) {
        requestReader = new RequestReader(inputStream, config);
    }

    public HttpRequest getHttpRequest() throws IOException {
        return requestReader.getHttpRequest();
    }


}
