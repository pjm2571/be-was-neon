package config;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

class ConfigTest {
    private Config config;

    @BeforeEach
    void setConfig() {
        config = new Config();
    }

    @Test
    @DisplayName("포트 번호가 올바르게 설정되었는지 확인")
    void testGetPort() {
        // SoftAssertions 객체 생성
        SoftAssertions softly = new SoftAssertions();

        // 포트 번호가 Integer 클래스의 인스턴스인지 확인
        softly.assertThat(config.getPort()).isInstanceOf(Integer.class);

        // 포트 번호가 8080인지 확인
        softly.assertThat(config.getPort()).isEqualTo(8080);

        // SoftAssertions를 닫고 결과 확인
        softly.assertAll();
    }

    @Test
    @DisplayName("encoding이 UTF-8로 올바르게 설정되었는지 확인")
    void testGetEncoding() {
        SoftAssertions softly = new SoftAssertions();

        // encoding 방식이 String 클래스의 인스턴스인지 확인
        softly.assertThat(config.getEncoding()).isInstanceOf(String.class);

        // encoding 방식이 UTF-8인지 확인
        softly.assertThat(config.getEncoding()).isEqualTo("UTF-8");

        // SoftAssertions를 닫고 결과 확인
        softly.assertAll();
    }

    @Test
    @DisplayName("static Route가 올바르게 설정되었는지 확인")
    void testGetStaticRoute() {
        SoftAssertions softly = new SoftAssertions();

        // static Route가 String 클래스의 인스턴스인지 확인
        softly.assertThat(config.getStaticRoute()).isInstanceOf(String.class);

        // static Route가 현재 프로젝트의 경로와 맞는 지 확인
        softly.assertThat(config.getStaticRoute()).isEqualTo("./src/main/resources/static");

        // SoftAssertions를 닫고 결과 확인
        softly.assertAll();
    }
}
