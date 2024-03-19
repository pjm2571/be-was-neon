package utils;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class QueryUtilsTest {
    @Test
    @DisplayName("QueryUtils map 리턴값 테스트")
    void queryTest(){
        String requestTarget = "/createUser?userId=id&password=pass&name=name";

        Map<String, String> queries = QueryUtils.getQueries(requestTarget);

        assertThat(queries.get("userId")).isEqualTo("id");
        assertThat(queries.get("password")).isEqualTo("pass");
        assertThat(queries.get("name")).isEqualTo("name");


    }
}
