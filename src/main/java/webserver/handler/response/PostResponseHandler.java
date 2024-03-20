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
import webserver.response.StaticFileResponse;
import webserver.utils.QueryUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class PostResponseHandler extends ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(PostResponseHandler.class);


    public PostResponseHandler(DataOutputStream responseWriter, Config config, HttpRequest httpRequest) {
        super(responseWriter, config, httpRequest);
    }

    @Override
    public void handleResponse() {
        handleCreate();

        String startLine = generateResponseStartLine(StatusCode.FOUND);
        String responseHeader = generateResponseHeader("/index.html");

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


    private String generateResponseHeader(String redirectTarget) {
        return "Location:" + SPACE + redirectTarget + CSRF + CSRF;
    }

    private void handleCreate() {
        String requestTarget = httpRequest.getRequestTarget();
        String requestBody = ((PostRequest) httpRequest).getRequestBody();

        if (requestTarget.startsWith(CREATE_USER)) {
            Map<String, String> queries = QueryUtils.getQueries(requestBody);
            User user = new User(queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email"));
            Database.addUser(user);
            logger.debug("user created : {}", user);
        }
    }

}
