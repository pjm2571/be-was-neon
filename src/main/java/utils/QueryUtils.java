package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryUtils {

    private final static String QUERY_SEPARATOR = "=";
    private final static String PARAM_SEPARATOR = "&";
    private final static String QUERY_SEQUENCE = "\\?";

    public static Map<String, String> getQueries(String requestTarget) {
        // requestTarget에서 "?" 이후의 문자열을 추출하여 파싱
        String queryString = requestTarget.split(QUERY_SEQUENCE)[1];
        
        // 쿼리 문자열을 "&"를 기준으로 분할하고 각각의 쿼리를 key=value 형식으로 매핑한 후 Map으로 수집
        return Arrays.stream(queryString.split(PARAM_SEPARATOR))
                .map(pair -> pair.split(QUERY_SEPARATOR))
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
    }


}
