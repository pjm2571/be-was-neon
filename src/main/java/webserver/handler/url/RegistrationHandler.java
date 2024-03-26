package webserver.handler.url;

import config.Config;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.WebServer;
import webserver.handler.UrlHandler;
import webserver.request.HttpRequest;
import webserver.request.message.HttpMethod;
import webserver.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;
import webserver.utils.QueryUtils;

import java.util.Map;

public class RegistrationHandler implements UrlHandler {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationHandler.class);

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, Config config) {

        return null;
    }


}
