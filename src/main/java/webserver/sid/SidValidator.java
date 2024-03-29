package webserver.sid;

import db.SessionStore;
import webserver.http.request.HttpRequest;

public class SidValidator {
    private static final String COOKIE = "Cookie";
    private static final String SID_START_LINE = "sid=";
    private static final String DEFAULT_FILE = "index.html";
    public static boolean isLoggedIn(HttpRequest httpRequest) {
        // index.html 파일만 다룬다.
        if (!isDefaultFile(httpRequest)) {
            return false;
        }

        try {
            String sid = getCookieSid(httpRequest);      // cookie의 sid 라인을 가져온다.
            return SessionStore.sessionIdExists(sid);   // SessionStore에 sid가 있다면 true, 없다면 false
        } catch (IllegalArgumentException e) {
            return false;       // 쿠키가 없거나, sid 값이 존재하지 않으면 false
        }
    }

    private static boolean isDefaultFile(HttpRequest httpRequest) {
        return httpRequest.getRequestLine().endsWith(DEFAULT_FILE);
    }


    public static String getCookieSid(HttpRequest httpRequest) {
        String cookie = httpRequest.getHeaderValue(COOKIE);

        if (cookie == null) {
            throw new IllegalArgumentException("[ERROR] 쿠키가 없습니다.");
        }

        int startIndex = cookie.indexOf(SID_START_LINE);

        if (startIndex == -1) {
            throw new IllegalArgumentException("[ERROR] Cookie의 Sid가 없습니다");
        }

        startIndex += SID_START_LINE.length();

        return cookie.substring(startIndex);
    }
}
