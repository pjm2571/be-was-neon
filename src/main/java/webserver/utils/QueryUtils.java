package webserver.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryUtils {

    private final static String QUERY_SEPARATOR = "=";
    private final static String PARAM_SEPARATOR = "&";
    private final static String ENCODING = "UTF-8";

    public static Map<String, String> getQueries(byte[] requestBody) {
        String queryLine;
        try {
            queryLine = URLDecoder.decode(new String(requestBody), ENCODING);
        } catch (UnsupportedEncodingException e) {
            queryLine = new String(requestBody);
        }
        return Arrays.stream(queryLine.split(PARAM_SEPARATOR))
                .map(pair -> pair.split(QUERY_SEPARATOR))
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
    }
}
