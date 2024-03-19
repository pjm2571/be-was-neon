package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.utils.HeaderUtils;

import static org.assertj.core.api.Assertions.*;


class HeaderUtilsTest {
    @Test
    @DisplayName("Request Header의 Key 추출 테스트")
    void getHeaderKeyTest(){
        String headerLine = "Host: www.lucas.com";

        assertThat(HeaderUtils.getHeaderKey(headerLine)).isEqualTo("Host");
    }
    
    @Test
    @DisplayName("Request Header의 Value 추출 테스트")
    void getHeaderValueTest(){
        String headerLine = "Host: www.lucas.com";

        assertThat(HeaderUtils.getHeaderValue(headerLine)).isEqualTo("www.lucas.com");
    }

}
