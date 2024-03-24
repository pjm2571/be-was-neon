package webserver.handler;

import config.Config;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public interface UrlHandler {
    HttpResponse handleRequest(HttpRequest httpRequest, Config config);

}
