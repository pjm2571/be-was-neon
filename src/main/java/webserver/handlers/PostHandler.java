package webserver.handlers;

import config.Config;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.response.HttpResponse;
import webserver.status.StatusCode;
import webserver.utils.QueryUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class PostHandler {
    private static final Logger logger = LoggerFactory.getLogger(PostHandler.class);

    private static final String CREATE_USER = "/createUser";
    private DataOutputStream dos;
    private HttpResponse httpResponse;
    private Config config;

    public PostHandler(DataOutputStream dos, HttpResponse httpResponse, Config config) {
        this.dos = dos;
        this.httpResponse = httpResponse;
        this.config = config;
    }

    public void handleResponse() {
        handleCreate(httpResponse.getRequestTarget(), httpResponse.getRequestBody());

        setResponse();

        try {
            dos.writeBytes(httpResponse.getStartLine());
            dos.writeBytes(httpResponse.getResponseHeader());

            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void setResponse() {
        httpResponse.setResponseHeader();
        httpResponse.setStartLine(StatusCode.FOUND);
    }

    private void handleCreate(String requestTarget, String requestBody) {
        if (requestTarget.startsWith(CREATE_USER)) {
            Map<String, String> queries = QueryUtils.getQueries(requestBody);
            User user = new User(queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email"));
            Database.addUser(user);
            logger.debug("user created : {}", user);
        }
    }
}
