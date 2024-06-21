package webserver.sid;

import java.util.Random;
import db.SessionStore;
public class SidGenerator {
    private static final int SID_LENGTH = 20;
    private static final int ASCII_START = 60;
    private static final int ASCII_LENGTH = 66;

    public static String getRandomSid() {
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
