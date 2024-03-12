package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.ContentType;

import static org.assertj.core.api.Assertions.*;

public class StringUtilsTest {

    private final static String ERROR_MESSAGE = "[ERROR]";

    @Test
    @DisplayName("request Line에서 PATH를 가져오는 테스트")
    void get_path_at_requestLine() {
        String httpRequestLine = "GET /img/ci_chevron-right.svg HTTP/1.1";

        String expectedPath = "/img/ci_chevron-right.svg";

        assertThat(StringUtils.getPath(httpRequestLine)).isEqualTo(expectedPath);
    }

    @Test
    @DisplayName("올바르지 않은 형식의 Request가 들어갈 경우 테스트")
    void get_path_at_invalid_requestLine() {
        String invalidHttpRequestLine = "GET /index.html";

        assertThatThrownBy(() -> StringUtils.getPath(invalidHttpRequestLine))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ERROR_MESSAGE);
    }

    @Test
    @DisplayName("request의 path에서 확장자 타입 가져오는 테스트")
    void get_type_at_requestPath() {
        String requestPath = "/img/ci_chevron-right.svg";

        assertThat(StringUtils.getType(requestPath)).isEqualTo(ContentType.svg.name());
    }

    @Test
    @DisplayName("request path에서 확장자 타입 불일치 테스트")
    void get_svg_type_error(){
        String requestPath = "/img/ci_chevron-right.svg";

        assertThat(StringUtils.getType(requestPath)).isNotEqualTo(ContentType.html.name());
    }

    @Test
    @DisplayName("create request의 url 파싱 테스트")
    void create_request_url_parsing(){
        String createRequsetLine = "/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        String expectedURL = "/create";

        assertThat(StringUtils.getURL(createRequsetLine)).isEqualTo(expectedURL);
    }

    @Test
    @DisplayName("create request의 인자 파싱 테스트")
    void create_request_params_parsing(){
        String createRequsetLine = "/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        String[] expectedParams = {"userId=javajigi", "password=password", "name=%EB%B0%95%EC%9E%AC%EC%84%B1", "email=javajigi%40slipp.net"};

        assertThat(StringUtils.getParams(createRequsetLine)).isEqualTo(expectedParams);
    }

}
