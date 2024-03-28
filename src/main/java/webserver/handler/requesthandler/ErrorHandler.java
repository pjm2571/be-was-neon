package webserver.handler.requesthandler;

import config.Config;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.utils.HttpResponseUtils;

public class ErrorHandler implements HttpRequestHandler {
    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, Config config) {
        return HttpResponseUtils.get404Response();
    }
}
