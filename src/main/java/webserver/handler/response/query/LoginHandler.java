package webserver.handler.response.query;

import db.Database;
import db.SessionStore;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.handler.response.StaticFileResponseHandler;
import webserver.response.HttpResponse;
import webserver.response.PostResponse;
import webserver.utils.QueryUtils;

import java.util.Map;

public class LoginHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String CRLF = "\r\n";
    private static final String SPACE = " ";

    private String requestBody;

    public LoginHandler(String requestBody) {
        this.requestBody = requestBody;
    }

    public HttpResponse getResponse() {
        String sid = getUserSid();

        String header = getHeader(sid);
        String startLine = generateResponseStartLine(StatusCode.FOUND);

        return new PostResponse(startLine, header);
    }

    private String generateResponseStartLine(StatusCode statusCode) {
        return HTTP_VERSION + SPACE + statusCode.getCode() + SPACE + statusCode.getDescription() + CRLF;
    }

    private String getHeader(String sid) {
        StringBuilder headerBuilder = new StringBuilder();

        if (sid == null) {
            logger.info("Login Failed!");
            headerBuilder.append(generateResponseHeader("/user/login_failed"));
            headerBuilder.append(CRLF);
            return headerBuilder.toString();
        }
        logger.info("Login Succeed");
        headerBuilder.append(generateResponseHeader("/"));
        headerBuilder.append(generateCookie(sid));

        return headerBuilder.toString();
    }

    private String generateResponseHeader(String redirectTarget) {
        return "Location:" + SPACE + redirectTarget + CRLF;
    }

    private String generateCookie(String sid) {
        return "Set-Cookie:" + SPACE + "sid=" + sid + ";" + SPACE + "Path=/" + CRLF + CRLF;
    }

    private String getUserSid() {
        Map<String, String> queries = QueryUtils.getQueries(requestBody);

        String inputId = queries.get("userId");
        String inputPassword = queries.get("password");
        User user = Database.findUserById(inputId);
        if (user == null) {
            logger.info("Not Registered User");
            return null;
        }

        if (!validateUser(user, inputId, inputPassword)) {
            logger.info("Registered But Invalid User");
            return null;
        }

        logger.info("Valid User");
        return SessionStore.getSessionByUser(user);
    }

    private boolean validateUser(User user, String inputId, String inputPassword) {
        return user.getPassword().equals(inputPassword) && user.getUserId().equals(inputId);
    }

}
