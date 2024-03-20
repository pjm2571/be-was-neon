package webserver.handler.response;

import config.Config;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.StatusCode;

import java.io.DataOutputStream;

public abstract class ResponseHandler {
    protected static final String CSRF = "\r\n";
    protected static final String SPACE = " ";
    protected static final String HTTP_VERSION = "HTTP/1.1";

    protected DataOutputStream responseWriter;
    protected Config config;
    protected HttpRequest httpRequest;

    public ResponseHandler(DataOutputStream responseWriter, Config config, HttpRequest httpRequest) {
        this.responseWriter = responseWriter;
        this.config = config;
        this.httpRequest = httpRequest;
    }

    public abstract void handleResponse();

    protected abstract void writeResponse(HttpResponse httpResponse);


    protected String generateResponseStartLine(StatusCode statusCode) {
        return HTTP_VERSION + SPACE + statusCode.getCode() + SPACE + statusCode.getDescription() + CSRF;
    }


}
