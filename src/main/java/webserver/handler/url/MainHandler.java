package webserver.handler.url;

import config.Config;
import webserver.Url;
import webserver.handler.UrlHandler;
import webserver.handler.error.ErrorHandler;
import webserver.handler.file.StaticFileHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.utils.HttpRequestUtils;

public class MainHandler implements UrlHandler {

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, Config config) {
        return switch (httpRequest.getMethod()) {
            case GET -> handleGet(httpRequest, config);
            case POST -> ErrorHandler.get404Response();
        };
    }

    private HttpResponse handleGet(HttpRequest httpRequest, Config config) {
        if (httpRequest.getRequestLine().equals(Url.MAIN.getUrlPath())) {   // url경로와 같다면 staticFileHandler!
            StaticFileHandler staticFileHandler = new StaticFileHandler();
            return staticFileHandler.handleRequest(HttpRequestUtils.convertToStaticFileRequest(httpRequest), config);
        }
        return ErrorHandler.get404Response();   // URL 이외의 요청은 404!
    }
}
