package webserver.router;

import java.io.*;

import config.Config;
import webserver.handler.request.*;
import webserver.utils.HttpRequestUtils;

public class RequestHandlerRouter {

    private BufferedReader requestReader;
    private Config config;

    public RequestHandlerRouter(InputStream in, Config config) throws UnsupportedEncodingException {
        this.requestReader = new BufferedReader(new InputStreamReader(in, config.getEncoding()));
        this.config = config;
    }

    public RequestHandler getRequestHandler() throws IOException {
        String startLine = requestReader.readLine();

        return extractRequestHandler(startLine);
    }


    private RequestHandler extractRequestHandler(String startLine) {
        // Post Request ( POST /create ...)
        if (HttpRequestUtils.isPostRequest(startLine)) {
            return new PostRequestHandler(requestReader, startLine);
        }

        String requestTarget = HttpRequestUtils.getRequestTarget(startLine);

        // Query Request ( /createUser?userId=id ...)
        if (HttpRequestUtils.isQueryRequest(requestTarget)) {
            return new QueryRequestHandler(requestReader, startLine);
        }

        // StaticFile Request ( .html .css .svg ...)
        if (HttpRequestUtils.isStaticFile(requestTarget)) {
            return new StaticFileRequestHandler(requestReader, startLine);
        }

        // UrlRequest ( /registration, /login ...)
        return new UrlRequestHandler(requestReader, startLine);
    }
}
