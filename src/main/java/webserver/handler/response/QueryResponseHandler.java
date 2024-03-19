package webserver.handler.response;

import config.Config;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.StatusCode;

import java.io.DataOutputStream;

public class QueryResponseHandler extends ResponseHandler {
    public QueryResponseHandler(DataOutputStream responseWriter, Config config, HttpRequest httpRequest) {
        super(responseWriter, config, httpRequest);
    }

    @Override
    public void handleResponse() {

    }

    @Override
    protected void writeResponse(HttpResponse httpResponse) {

    }

    @Override
    protected String generateResponseStartLine(StatusCode statusCode) {
        return null;
    }
}
