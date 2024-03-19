package utils;

public class HeaderUtils {
    private final static String HEADER_SEPARATOR = ":";

    private HeaderUtils() {

    }

    public static String getHeaderKey(String headerLine) {
        String[] tokens = headerLine.split(HEADER_SEPARATOR);
        return tokens[0];
    }

    public static String getHeaderValue(String headerLine) {
        String[] tokens = headerLine.split(HEADER_SEPARATOR);
        return tokens[1].trim();
    }


}
