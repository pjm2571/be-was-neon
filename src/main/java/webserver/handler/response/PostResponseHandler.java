package webserver.handler.response;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import config.Config;
import db.Database;
import model.User;
import webserver.request.HttpRequest;
import webserver.request.PostRequest;
import webserver.response.HttpResponse;
import webserver.StatusCode;
import webserver.response.PostResponse;
import webserver.utils.QueryUtils;

import javax.swing.text.html.CSS;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.cert.CRL;
import java.util.Map;

public class PostResponseHandler extends ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(PostResponseHandler.class);


    public PostResponseHandler(DataOutputStream responseWriter, Config config, HttpRequest httpRequest) {
        super(responseWriter, config, httpRequest);
    }

    @Override
    public void handleResponse() {
        String sid = handleCreate();

        String startLine = generateResponseStartLine(StatusCode.FOUND);
        String responseHeader = generateResponseHeader(sid, "/index.html");

        HttpResponse httpResponse = new PostResponse(startLine, responseHeader);

        writeResponse(httpResponse);
    }

    @Override
    protected void writeResponse(HttpResponse httpResponse) {
        try {
            responseWriter.writeBytes(httpResponse.getStartLine());
            responseWriter.writeBytes(httpResponse.getRequestHeader());

            responseWriter.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    private String generateResponseHeader(String sid, String redirectTarget) {
        return "Location:" + SPACE + redirectTarget + CRLF +
                "Set-Cookie:" + SPACE + "sid=" + SPACE + sid + ";" + SPACE + "Path=/" + CRLF + CRLF;
    }

    private String handleCreate() {
        String requestTarget = httpRequest.getRequestTarget();
        String requestBody = ((PostRequest) httpRequest).getRequestBody();

        CreateHandler createHandler = new CreateHandler(requestTarget, requestBody);
        return createHandler.create();
    }

}
