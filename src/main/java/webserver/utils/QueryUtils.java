package webserver.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryUtils {

    private final static String QUERY_SEPARATOR = "=";
    private final static String PARAM_SEPARATOR = "&";
    private final static String QUERY_SEQUENCE = "\\?";

    public static String getQueryLine(String queryLine) {
        String[] tokens = queryLine.split(QUERY_SEPARATOR);
        return tokens[1];
    }

    public static Map<String, String> getQueries(String queryLine) {
        // requestTarget에서 "?" 이후의 문자열을 추출하여 파싱
        // 쿼리 문자열을 "&"를 기준으로 분할하고 각각의 쿼리를 key=value 형식으로 매핑한 후 Map으로 수집
        return Arrays.stream(queryLine.split(PARAM_SEPARATOR))
                .map(pair -> pair.split(QUERY_SEPARATOR))
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
    }


}
