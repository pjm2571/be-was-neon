package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.StringUtils;

import java.util.Map;

public class CreateHandler {
    private static final Logger logger = LoggerFactory.getLogger(CreateHandler.class);

    private String requestUrl; // 유저가 아닌 다른 create를 위해 남겨두자

    private Map<String, String> params;

    CreateHandler(String requestTarget) {
        this.requestUrl = StringUtils.getRequestUrl(requestTarget);
        this.params = StringUtils.getParams(requestTarget);
    }

    public void create() {
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        Database.addUser(user);
        logger.debug("create : {}", user.toString());
    }


}
