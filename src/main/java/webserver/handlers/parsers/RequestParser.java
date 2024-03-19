package webserver.handlers.parsers;

import webserver.utils.RequestUtils;
import webserver.request.*;

public class RequestParser {
    private String startLine;

    public RequestParser(String startLine) {
        this.startLine = startLine;
    }

    public HttpRequest extractRequest() {

        if (RequestUtils.isPostRequest(startLine)) {
            return new PostRequest(startLine);
        }

        // requestTarget을 가져온다.
        String requestTarget = RequestUtils.getRequestTarget(startLine);

        // - 쿼리문인지 판별
        if (RequestUtils.isQueryRequest(requestTarget)) {
            return new QueryRequest(startLine);
        }

        // - static 인지 판별
        if (RequestUtils.isStaticFile(requestTarget)) {
            return new StaticFileRequest(startLine);
        }

        // - url인지 판별 -> url이라면 .html을 붙여주어야 한다.
        if (RequestUtils.isUrl(requestTarget)) {
            return new UrlRequest(startLine);
        }

        // 적절한 요청이 아니라면 null 반환
        return null;
    }


}
