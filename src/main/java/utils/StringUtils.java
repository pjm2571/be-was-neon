package utils;

public class StringUtils {
    private StringUtils() {

    }

    private static final String ERROR = "[ERROR]";
    private static final String REQUEST_ERROR = ERROR + " 올바르지 않은 형식의 Request 입니다.";
    public static final String SPACE = " ";
    public static final String DOT = "\\.";

    public static String getPath(String inputLine) {
        String[] tokens = inputLine.split(SPACE);
        if (tokens.length != 3) {
            throw new IllegalArgumentException(REQUEST_ERROR);
        }
        return tokens[1];
    }

    public static String getType(String inputLine) {
        String[] tokens = inputLine.split(DOT);
        return tokens[1];
    }
}
