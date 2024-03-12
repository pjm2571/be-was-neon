package utils;

public class StringUtils {
    private StringUtils() {

    }

    private static final String ERROR = "[ERROR]";
    private static final String REQUEST_ERROR = ERROR + " 올바르지 않은 형식의 Request 입니다.";
    public static final String SPACE = " ";
    public static final String DOT = "\\.";
    public static final String QUESTION = "\\?";
    public static final String AND = "&";
    public static final String EQUAL = "=";

    public static String getPath(String inputLine) {
        String[] tokens = inputLine.split(SPACE);
        if (tokens.length != 3) {
            throw new IllegalArgumentException(REQUEST_ERROR);
        }
        return tokens[1];
    }

    //create?userId=z&name=a&password=b&email=pj%40nave.com
    public static String getURL(String inputLine) {
        String[] tokens = inputLine.split(QUESTION);   // 물음표를 기준으로 앞의 문자열만 가져온다.
        return tokens[0];
    }

    public static String[] getParams(String inputLine) {
        String path = getPath(inputLine);
        String paramLine = path.split(QUESTION)[1]; // 물음표를 기준으로 뒤의 문자열만 가져온다.
        return paramLine.split(AND);
    }


    public static String getType(String inputLine) {
        String[] tokens = inputLine.split(DOT);
        return tokens[1];
    }

    public static String getParamValue(String inputParamLine) {
        String[] tokens = inputParamLine.split(EQUAL);
        return tokens[1];
    }
}
