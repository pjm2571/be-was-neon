import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import static webserver.CONTENT_TYPE.*;

/* CONTENT_TYPE Enum 테스트 */
public class CONTENT_TYPE_Test {
    @Test
    @DisplayName("HTML 콘텐트 타입 정상 리턴 테스트")
    void html_type_test(){
        String type = "html";

        String expectedContentType = "Content-Type: text/html;charset=utf-8\r\n";

        assertThat(getContentType(type)).isEqualTo(expectedContentType);
    }

    @Test
    @DisplayName("CSS 콘텐트 타입 정상 리턴 테스트")
    void css_type_test(){
        String type = "css";

        String expectedContentType = "Content-Type: text/css;charset=utf-8\r\n";

        assertThat(getContentType(type)).isEqualTo(expectedContentType);
    }

    @Test
    @DisplayName("SVG 콘텐트 타입 정상 리턴 테스트")
    void svg_type_test(){
        String type = "svg";

        String expectedContentType = "Content-Type: image/svg+xml\r\n";

        assertThat(getContentType(type)).isEqualTo(expectedContentType);
    }

    @Test
    @DisplayName("ico 콘텐트 타입 정상 리턴 테스트")
    void ico_type_test(){
        String type = "ico";

        String expectedContentType = "Content-Type: image/x-icon\r\n";

        assertThat(getContentType(type)).isEqualTo(expectedContentType);
    }

    @Test
    @DisplayName("html 콘텐트 타입과 css 콘텐트 타입 미스매치 테스트")
    void type_miss_match_test(){
        String type = "html";

        String cssContentType = "Content-Type: text/css;charset=utf-8\r\n";

        assertThat(getContentType(type)).isNotEqualTo(cssContentType);
    }


}
