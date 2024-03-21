package webserver.handler.response;

import config.Config;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.StatusCode;
import webserver.response.PostResponse;
import webserver.utils.QueryUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class QueryResponseHandler extends ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(QueryResponseHandler.class);

    public QueryResponseHandler(DataOutputStream responseWriter, Config config, HttpRequest httpRequest) {
        super(responseWriter, config, httpRequest);
    }

    @Override
    public void handleResponse() {
        handleCreate();

        String startLine = generateResponseStartLine(StatusCode.FOUND);
        String responseHeader = generateResponseHeader("/index.html");

        HttpResponse httpResponse = new PostResponse(startLine, responseHeader);

        writeRedirectResponse(httpResponse);
    }

    private void writeRedirectResponse(HttpResponse httpResponse) {
        try {
            responseWriter.writeBytes(httpResponse.getStartLine());
            responseWriter.writeBytes(httpResponse.getRequestHeader());

            responseWriter.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String generateResponseHeader(String redirectTarget) {
        return "Location:" + SPACE + redirectTarget + CRLF + CRLF;
    }

    private void handleCreate() {
        String requestTarget = httpRequest.getRequestTarget();
        System.out.println("q : " + requestTarget);

        String queryLine = QueryUtils.getQueryLine(requestTarget);

        if (requestTarget.startsWith(CREATE_USER)) {
            System.out.println("q : " + queryLine);
            Map<String, String> queries = QueryUtils.getQueries(queryLine);
            User user = new User(queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email"));
            Database.addUser(user);
            logger.debug("user created : {}", user);
        }
    }
}
