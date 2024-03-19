package webserver.handlers;

import config.Config;
import db.Database;
import model.User;
import webserver.utils.QueryUtils;
import webserver.response.HttpResponse;
import webserver.status.StatusCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.Map;

public class QueryHandler {
    private static final Logger logger = LoggerFactory.getLogger(QueryHandler.class);

    private static final String CREATE_USER = "/createUser";
    private DataOutputStream dos;
    private HttpResponse httpResponse;
    private Config config;

    public QueryHandler(DataOutputStream dos, HttpResponse httpResponse, Config config) {
        this.dos = dos;
        this.httpResponse = httpResponse;
        this.config = config;
    }

    public void handleResponse() {
        handleCreate(httpResponse.getRequestTarget());

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

    private void handleCreate(String requestTarget) {
        if (requestTarget.startsWith(CREATE_USER)) {
            Map<String, String> queries = QueryUtils.getQueries(requestTarget);
            User user = new User(queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email"));
            Database.addUser(user);
            logger.debug("user created : {}", user);
        }
    }
}
