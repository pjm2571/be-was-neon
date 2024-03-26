package webserver.handler;

import config.Config;
import webserver.Url;
import webserver.constants.Constants;
import webserver.handler.error.ErrorHandler;
import webserver.handler.file.StaticFileHandler;
import webserver.request.HttpRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.message.HttpRequestStartLine;
import webserver.response.HttpResponse;
import webserver.utils.HttpRequestUtils;

import static webserver.utils.HttpRequestUtils.convertToStaticFileRequest;

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

        String requestLine = httpRequest.getRequestLine();


        // 1) 정적 파일일 경우!
        if (HttpRequestUtils.isStaticFile(requestLine)) {
            StaticFileHandler staticFileHandler = new StaticFileHandler();
            return staticFileHandler.handleRequest(httpRequest, config);
        }

        // 2) 정적 파일이 아닌 경우 동적으로 동작

        // 2-1) Root Url일 경우 바로 Static으로 동작
        if (HttpRequestUtils.isRootUrl(requestLine)) {
            StaticFileHandler staticFileHandler = new StaticFileHandler();
            return staticFileHandler.handleRequest(convertToStaticFileRequest(httpRequest), config);
        }

        // 2-2) requestLine을 URL로 파싱한다.
        try {
            // 1) Url 경로를 파악한다
            // -> 경로에 없는 Url일 경우에는 IllegalArgumentException [404] 에러!
            Url url = HttpRequestUtils.getUrl(requestLine);
            logger.debug("Url : {}", url);

            // 2) Url Factory를 통해 Url Handler를 받아온다
            UrlHandlerFactory factory = new UrlHandlerFactory();
            UrlHandler urlHandler = factory.createUrlHandler(url);

            // 3) Url Handler가 동작하여 역할 수행 후, Response를 생성한다.
            return urlHandler.handleRequest(httpRequest, config);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return ErrorHandler.get404Response();
        }


    }


}
