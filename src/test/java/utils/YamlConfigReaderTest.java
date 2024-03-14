package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class YamlConfigReaderTest {
    private final static String STATIC = "./src/main/resources/static";
    private final static int PORT = 8080;

    @Test
    @DisplayName("YamlConfigFile에서 설정해둔 static 경로와 일치 테스트")
    void getStaticSourceFromYaml(){
        assertThat(YamlConfigReader.loadStaticSourcePathFromYaml()).isEqualTo(STATIC);
    }

    @Test
    @DisplayName("YamlConfigFile에서 설정해둔 static 경로 불일치 테스트")
    void getStaticSourceError(){
        String templateSource = "./src/main/resources/templates";

        assertThat(YamlConfigReader.loadStaticSourcePathFromYaml()).isNotEqualTo(templateSource);
    }

    @Test
    @DisplayName("YamlConfigFile에서 설정해둔 port 번호와 일치 테스트")
    void getPortFromYaml(){
        assertThat(YamlConfigReader.loadPortFromYaml()).isEqualTo(PORT);
    }

    @Test
    @DisplayName("YamlConfigFile의 포트 번호 불일치 테스트")
    void getPortError(){
        int errorPort = 1;
        assertThat(YamlConfigReader.loadPortFromYaml()).isNotEqualTo(errorPort);
    }
}
