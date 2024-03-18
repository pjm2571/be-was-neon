package webserver.handlers.parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RequestUtils;
import webserver.handlers.readers.RequestReader;
import webserver.handlers.request.HttpRequest;
import webserver.handlers.request.QueryRequest;
import webserver.handlers.request.StaticFileRequest;
import webserver.handlers.request.UrlRequest;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    private String startLine;

    public RequestParser(String startLine) {
        this.startLine = startLine;
    }

    public HttpRequest extractRequest() {
        // requestTarget을 가져온다.
        String requestTarget = RequestUtils.getRequestTarget(startLine);

        // - 쿼리문인지 판별
        if (RequestUtils.isQueryRequest(requestTarget)) {
            logger.info("QueryRequest : {}", startLine);
            return new QueryRequest(startLine);
        }

        // - static 인지 판별
        if (RequestUtils.isStaticFile(requestTarget)) {
            logger.info("StaticFileRequest : {}", startLine);
            return new StaticFileRequest(startLine);
        }

        // - url인지 판별 -> url이라면 .html을 붙여주어야 한다.
        if (RequestUtils.isUrl(requestTarget)) {
            logger.info("UrlRequest : {}", startLine);
            return new UrlRequest(parseUrl(startLine));
        }

        // 적절한 요청이 아니라면 null 반환
        return null;
    }

    private String parseUrl(String startLine) {
        StringBuilder sb = new StringBuilder();

        sb.append(RequestUtils.getHttpMethod(startLine)).append(" ");
        sb.append(RequestUtils.getRequestTarget(startLine));

        if (!RequestUtils.getRequestTarget(startLine).endsWith("/")) {
            sb.append("/");
        }

        sb.append("index.html").append(" ");
        sb.append(RequestUtils.getHttpVersion(startLine));
        return sb.toString();
    }
}
