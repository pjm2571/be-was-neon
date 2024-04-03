package webserver.handler.requesthandler;

import webserver.StatusCode;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.sid.SidValidator;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;

public class ArticleHandler implements HttpRequestHandler {
    private static final String REDIRECT_URL = "/";


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
        if (SidValidator.isLoggedIn(httpRequest)) {
            StaticFileHandler staticFileHandler = new StaticFileHandler();
            return staticFileHandler.handleRequest(httpRequest);
        }
        String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.FOUND);
        String header = HttpResponseUtils.generateRedirectResponseHeader(REDIRECT_URL);
        return new HttpResponse(startLine, header);
    }

    private HttpResponse handlePost(HttpRequest httpRequest) {
        return HttpResponseUtils.get500Response();
    }


}
