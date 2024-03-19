package webserver.handlers;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.response.HttpResponse;
import webserver.response.QueryResponse;
import webserver.response.StaticFileResponse;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class ResponseHandler {

    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    private DataOutputStream dos;
    private HttpResponse httpResponse;
    private final Config config;

    public ResponseHandler(OutputStream outputStream, HttpResponse httpResponse, Config config) {
        this.dos = new DataOutputStream(outputStream);
        this.httpResponse = httpResponse;
        this.config = config;
    }


    public void handleResponse() {
        if (httpResponse instanceof QueryResponse) {
            QueryHandler queryHandler = new QueryHandler(dos, httpResponse, config);
            queryHandler.handleResponse();
        }
        if (httpResponse instanceof StaticFileResponse) {
            StaticFileHandler staticFileHandler = new StaticFileHandler(dos, httpResponse, config);
            staticFileHandler.handleResponse();
        }
    }

}
