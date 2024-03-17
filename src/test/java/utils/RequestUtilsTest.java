package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RequestUtilsTest {

    @Test
    @DisplayName("HttpMethod 파싱 성공 테스트")
    void testGetHttpMethod() {
        String startLine = "GET /index.html HTTP/1.1";
        assertThat(RequestUtils.getHttpMethod(startLine)).isEqualTo("GET");
    }

    @Test
    @DisplayName("HttpRequestTarget 파싱 성공 테스트")
    void testGetRequestTarget() {
        String startLine = "GET /index.html HTTP/1.1";
        assertThat(RequestUtils.getRequestTarget(startLine)).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("HttpVersion 파싱 성공 테스트")
    void testGetHttpVersion() {
        String startLine = "GET /index.html HTTP/1.1";
        assertThat(RequestUtils.getHttpVersion(startLine)).isEqualTo("HTTP/1.1");
    }

    @Test
    @DisplayName("isQueryRequest 성공 테스트")
    void testIsQueryRequest() {
        String requestTargetWithQuery = "/index.html?param=value";
        assertThat(RequestUtils.isQueryRequest(requestTargetWithQuery)).isTrue();

        String requestTargetWithoutQuery = "/index.html";
        assertThat(RequestUtils.isQueryRequest(requestTargetWithoutQuery)).isFalse();
    }

    @Test
    @DisplayName("isStaticFile 성공 테스트")
    void testIsStaticFile() {
        String staticFileWithHtmlExtension = "/index.html";
        assertThat(RequestUtils.isStaticFile(staticFileWithHtmlExtension)).isTrue();

        String staticFileWithCssExtension = "/styles.css";
        assertThat(RequestUtils.isStaticFile(staticFileWithCssExtension)).isTrue();
    }

    @Test
    @DisplayName("isUrl 성공 테스트")
    void testIsUrl() {
        String url = "/path/to/some/resource";
        assertThat(RequestUtils.isUrl(url)).isTrue();
    }

    @Test
    @DisplayName("isUrl 실패 테스트")
    void testIsUrl_False() {
        String staticFileWithHtmlExtension = "/index.html";
        assertThat(RequestUtils.isUrl(staticFileWithHtmlExtension)).isFalse();

        String queryRequest = "/search?q=keyword";
        assertThat(RequestUtils.isUrl(queryRequest)).isFalse();
    }

}
