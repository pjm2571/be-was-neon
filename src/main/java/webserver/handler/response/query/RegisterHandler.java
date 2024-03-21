package webserver.handler.response.query;

import db.Database;
import db.SessionStore;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.handler.response.PostResponseHandler;
import webserver.response.HttpResponse;
import webserver.response.PostResponse;
import webserver.utils.QueryUtils;

import java.security.cert.CRL;
import java.util.Map;
import java.util.Random;

public class RegisterHandler {
    private static final Logger logger = LoggerFactory.getLogger(PostResponseHandler.class);
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String SPACE = " ";
    private static final String CRLF = "\r\n";

    private static final int SID_LENGTH = 20;
    private static final int ASCII_START = 60;
    private static final int ASCII_LENGTH = 66;
    private String requestBody;

    public RegisterHandler(String requestBody) {
        this.requestBody = requestBody;
    }

    private void registerUser() {
        User user = createUser();   // User 객체를 생성
        Database.addUser(user); // DB에 저장
        logger.debug("user created : {}", user);
        String randomSid = getRandomSid();
        SessionStore.addSession(randomSid, user);  // Session 저장소에 저장
    }

    public HttpResponse getResponse() {
        registerUser();
        String startLine = generateResponseStartLine(StatusCode.FOUND);
        String header = generateResponseHeader("/index.html");
        return new PostResponse(startLine, header);
    }

    private String generateResponseStartLine(StatusCode statusCode) {
        return HTTP_VERSION + SPACE + statusCode.getCode() + SPACE + statusCode.getDescription() + CRLF;
    }

    private String generateResponseHeader(String redirectTarget) {
        return "Location:" + SPACE + redirectTarget + CRLF + CRLF;
    }

    private User createUser() {
        Map<String, String> queries = QueryUtils.getQueries(requestBody);
        User user = new User(queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email"));
        return user;
    }


    private String getRandomSid() {
        while (true) {
            StringBuilder sb = new StringBuilder(SID_LENGTH);
            Random random = new Random();

            for (int i = 0; i < SID_LENGTH; i++) {
                char randomChar = (char) (ASCII_START + random.nextInt(ASCII_LENGTH));  // 32 ~ 126 까지의 ascii code
                sb.append(randomChar);
            }

            if (!SessionStore.sessionIdExists(sb.toString())) {
                return sb.toString();
            }
        }
    }

}
