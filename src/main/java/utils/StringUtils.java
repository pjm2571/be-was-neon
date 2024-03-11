package utils;

public class StringUtils {
    private StringUtils() {

    }

    public static final String SPACE = " ";

    public static String getFirstToken(String inputLine) {
        String[] tokens = inputLine.split(SPACE);
        return tokens[1];
    }
}
