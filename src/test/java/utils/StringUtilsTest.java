package utils;

import org.junit.jupiter.api.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class StringUtilsTest {
    @Test
    @DisplayName("Request Line에서 httpMethod를 파싱하는 테스트")
    void parseHttpMethod_test() {
        String requestLine = "GET /index.html HTTP/1.1";

        assertThat(StringUtils.getHttpMethod(requestLine)).isEqualTo("GET");
    }

    @Test
    @DisplayName("Request Line에서 requestTarget을 파싱하는 테스트")
    void parseRequestTarget_test() {
        String requestLine = "GET /index.html HTTP/1.1";

        assertThat(StringUtils.getRequestTarget(requestLine)).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("Request Line에서 httpVersion을 파싱하는 테스트")
    void parseHttpVersion_test() {
        String requestLine = "GET /index.html HTTP/1.1";

        assertThat(StringUtils.getHttpVersion(requestLine)).isEqualTo("HTTP/1.1");
    }

    @Test
    @DisplayName("Request Target에서 url을 파싱하는 테스트")
    void parseUrl_test() {
        String requestTarget = "/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        assertThat(StringUtils.getRequestUrl(requestTarget)).isEqualTo("/create");
    }

    @Test
    @DisplayName("Request Target에서 파라미터들을 파싱하는 테스트")
    void parseParams_test() {
        String requestTarget = "/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        Map<String, String> expectedParams = Map.of(
                "userId", "javajigi",
                "password", "password",
                "name", "%EB%B0%95%EC%9E%AC%EC%84%B1",
                "email", "javajigi%40slipp.net"
        );

        assertThat(StringUtils.getParams(requestTarget)).isEqualTo(expectedParams);
    }
}
