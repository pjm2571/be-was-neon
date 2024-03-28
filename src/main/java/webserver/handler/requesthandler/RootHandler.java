package webserver.handler.requesthandler;

import config.Config;
import webserver.handler.HttpRequestHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;

public class RootHandler implements HttpRequestHandler {

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, Config config) {
        switch (httpRequest.getMethod()) {
            case GET -> {
                return handleGet(httpRequest, config);
            }
            default -> {
                return HttpResponseUtils.get404Response();  // GET 요쳥이 아닌 경우
            }
        }
    }

    private HttpResponse handleGet(HttpRequest httpRequest, Config config) {
        httpRequest = HttpRequestUtils.convertToStaticFileRequest(httpRequest);
        StaticFileHandler staticFileHandler = new StaticFileHandler();
        return staticFileHandler.handleRequest(httpRequest, config);
    }
}
