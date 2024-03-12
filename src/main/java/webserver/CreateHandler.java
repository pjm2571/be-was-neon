package webserver;

import db.Database;
import model.User;
import utils.StringUtils;

import java.util.Arrays;

public class CreateHandler {
    private String url; // 유저가 아닌 다른 create를 위해 남겨두자
    private String[] params;

    CreateHandler(String requestLine) {
        this.url = StringUtils.getURL(requestLine);
        this.params = getParamsValue(requestLine);
    }

    private String[] getParamsValue(String requestLine) {
        return Arrays.stream(StringUtils.getParams(requestLine))
                .map(StringUtils::getParamValue)
                .toArray(String[]::new);
    }

    public void create() {
        User user = new User(params[0], params[1], params[2], params[3]);
        Database.addUser(user);
    }


}
