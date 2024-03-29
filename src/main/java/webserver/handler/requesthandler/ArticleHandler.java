package webserver.handler.requesthandler;

import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;

public class ArticleHandler implements HttpRequestHandler {


    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) {
        switch (httpRequest.getMethod()) {
            case GET -> {
                return handleGet(httpRequest);
            }
            case POST -> {
                return handlePost(httpRequest);
            }
            default -> {
                return HttpResponseUtils.get404Response();  // GET, POST 요쳥이 아닌 경우
            }
        }
    }

    private HttpResponse handleGet(HttpRequest httpRequest) {
        httpRequest = HttpRequestUtils.convertToStaticFileRequest(httpRequest);
        StaticFileHandler staticFileHandler = new StaticFileHandler();
        return staticFileHandler.handleRequest(httpRequest);
    }

    private HttpResponse handlePost(HttpRequest httpRequest) {
        return HttpResponseUtils.get500Response();
    }

}
