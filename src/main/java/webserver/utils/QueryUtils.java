package webserver.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryUtils {

    private final static String QUERY_SEPARATOR = "=";
    private final static String PARAM_SEPARATOR = "&";

    public static Map<String, String> getQueries(String queryLine) {
        return Arrays.stream(queryLine.split(PARAM_SEPARATOR))
                .map(pair -> pair.split(QUERY_SEPARATOR))
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
    }


}
