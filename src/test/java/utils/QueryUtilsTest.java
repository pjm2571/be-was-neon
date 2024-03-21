package utils;


import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.utils.QueryUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class QueryUtilsTest {

    @Test
    @DisplayName("get query request의 queryline 파싱 테스트")
    void getQueryLineTest() {
        String queryRequest = "/createUser?userId=id&password=pass&name=name&email=zoonmy%40naver.com";

        assertThat(QueryUtils.getQueryLine(queryRequest)).isEqualTo("userId=id&password=pass&name=name&email=zoonmy%40naver.com");
    }

    @Test
    @DisplayName("query문 쿼리 요소들 정상 파싱 테스트")
    void getQueriesTest() {
        SoftAssertions softly = new SoftAssertions();

        String requestTarget = "/createUser?userId=id&password=pass&name=name&email=zoonmy%40naver.com";

        Map<String, String> queries = QueryUtils.getQueries(requestTarget);

        softly.assertThat(queries.get("userId")).isEqualTo("id");
        softly.assertThat(queries.get("password")).isEqualTo("pass");
        softly.assertThat(queries.get("name")).isEqualTo("name");
        softly.assertThat(queries.get("email")).isEqualTo("zoonmy%40naver.com");
    }
}
