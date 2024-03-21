package webserver.parser;

import java.io.*;

import config.Config;
import webserver.handler.request.*;
import webserver.utils.RequestUtils;

public class RequestHandlerParser {

    private BufferedReader requestReader;
    private Config config;

    public RequestHandlerParser(InputStream in, Config config) throws UnsupportedEncodingException {
        this.requestReader = new BufferedReader(new InputStreamReader(in, config.getEncoding()));
        this.config = config;
    }

    public RequestHandler getRequestHandler() throws IOException {
        String startLine = requestReader.readLine();

        return extractRequestHandler(startLine);
    }


    private RequestHandler extractRequestHandler(String startLine) {
        // Post Request ( POST /create ...)
        if (RequestUtils.isPostRequest(startLine)) {
            return new PostRequestHandler(requestReader, startLine);
        }

        String requestTarget = RequestUtils.getRequestTarget(startLine);

        // Query Request ( /createUser?userId=id ...)
        if (RequestUtils.isQueryRequest(requestTarget)) {
            return new QueryRequestHandler(requestReader, startLine);
        }

        // StaticFile Request ( .html .css .svg ...)
        if (RequestUtils.isStaticFile(requestTarget)) {
            return new StaticFileRequestHandler(requestReader, startLine);
        }

        // UrlRequest ( /registration, /login ...)
        return new UrlRequestHandler(requestReader, startLine);
    }
}
