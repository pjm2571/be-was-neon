package webserver.parser;

import config.Config;
import webserver.handler.response.*;
import webserver.request.*;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class ResponseHandlerParser {
    private DataOutputStream responseWriter;
    private Config config;
    private HttpRequest httpRequest;

    public ResponseHandlerParser(OutputStream out, Config config, HttpRequest httpRequest) {
        this.responseWriter = new DataOutputStream(out);
        this.config = config;
        this.httpRequest = httpRequest;
    }

    public ResponseHandler getResponseHandler() {
        if (httpRequest instanceof QueryRequest) {
            return new QueryResponseHandler(responseWriter, config, httpRequest);
        }
        if (httpRequest instanceof PostRequest) {
            return new PostResponseHandler(responseWriter, config, httpRequest);
        }
        return new StaticFileResponseHandler(responseWriter, config, httpRequest);
    }

}
