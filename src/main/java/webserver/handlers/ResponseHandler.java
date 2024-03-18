package webserver.handlers;

import config.Config;
import webserver.handlers.request.HttpRequest;
import webserver.handlers.response.HttpResponse;
import webserver.handlers.response.QueryResponse;
import webserver.handlers.response.StaticFileResponse;
import webserver.handlers.writers.ResponseWriter;

import java.io.OutputStream;

public class ResponseHandler {

    private ResponseWriter responseWriter;
    private HttpResponse httpResponse;
    private final String staticRoute;

    public ResponseHandler(OutputStream outputStream, HttpResponse httpResponse, Config config) {
        this.responseWriter = new ResponseWriter(outputStream);
        this.httpResponse = httpResponse;
        this.staticRoute = config.getStaticRoute();
    }

    public void handleResponse() {
        if (httpResponse instanceof QueryResponse) {
        }
        if (httpResponse instanceof StaticFileResponse) {

        }
    }


}
