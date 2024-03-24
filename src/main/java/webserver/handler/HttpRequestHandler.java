package webserver.handler;

import config.Config;
import webserver.Url;
import webserver.request.HttpRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.response.HttpResponse;
import webserver.utils.HttpRequestUtils;

public class HttpRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);
    private HttpRequest httpRequest;

    public HttpRequestHandler(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    // 1) main path
    // 2) login path
    // 3) register path
    // ...

    public HttpResponse handleRequest(Config config) {
        try {
            Url url = HttpRequestUtils.getUrl(httpRequest.getRequestLine());
            logger.debug("Url : {}", url);

            UrlHandlerFactory factory = new UrlHandlerFactory();

            UrlHandler urlHandler = factory.createUrlHandler(url);

            return urlHandler.handleRequest(httpRequest, config);
        } catch (IllegalArgumentException e) {
            // Url이 없는 경우 not found! 404
            logger.error(e.getMessage());
            // 404 처리!
            return null;
        }
    }


}
