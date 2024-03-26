package db;

import model.User;

import java.util.HashMap;
import java.util.Map;

public class SessionStore {
    private static Map<String, User> sessions = new HashMap<>();

    public static boolean sessionIdExists(String sid) {
        return sessions.keySet().stream()
                .anyMatch(sessionId -> sessionId.equals(sid));
    }

    public static void expireSid(String sid) {
        sessions.remove(sid);
    }

    public static void addSession(String sessionId, User user) {
        sessions.put(sessionId, user);
    }

    public static String getSessionByUser(User user) {
        return sessions.entrySet().stream()
                .filter(entry -> entry.getValue().equals(user))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
