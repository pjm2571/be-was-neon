package webserver.handler.response;

import db.Database;
import db.SessionStore;
import model.User;
import webserver.utils.QueryUtils;

import java.util.Map;
import java.util.Random;

public class CreateHandler {
    private static final int SID_LENGTH = 20;
    private static final int ASCII_START = 32;
    private static final int ASCII_LENGTH = 94;
    private static final String CREATE_USER = "/createUser";

    private String requestTarget;
    private String requestBody;

    CreateHandler(String requestTarget, String requestBody) {
        this.requestTarget = requestTarget;
        this.requestBody = requestBody;
    }

    public String create() {
        if (requestTarget.startsWith(CREATE_USER)) {
            User user = createUser();   // User 객체를 생성
            Database.addUser(user); // DB에 저장
            String randomSid = getRandomSid();
            SessionStore.addSession(randomSid, user);  // Session 저장소에 저장
            return randomSid;
        }

        // 다른 POST Create 문을 위해 남겨두자
        return "";
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
