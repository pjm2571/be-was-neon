package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import webserver.CreateHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static final String YAML_FILE = "config.yaml";

    private String encoding;
    private int port;
    private String staticRoute;

    public Config() {
        Map<String, Object> yamlConfigs = getYamlConfigs();

        setEncoding(yamlConfigs);
        setPort(yamlConfigs);
        setStaticRoute(yamlConfigs);
    }

    private Map<String, Object> getYamlConfigs() {
        try {
            InputStream inputStream = new FileInputStream(YAML_FILE);
            Yaml yaml = new Yaml();
            return yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            logger.error("[ERROR] config.yaml 파일을 찾을 수 없습니다: " + YAML_FILE);
            throw new IllegalArgumentException();
        }
    }

    private void setEncoding(Map<String, Object> yamlConfigs) {
        Object encoding = yamlConfigs.get("encoding");
        if (encoding == null) {
            logger.error("[ERROR] yaml.file의 encoding 라인 읽기 에러");
            throw new IllegalArgumentException();
        }
        this.encoding = (String) encoding;
    }

    private void setPort(Map<String, Object> yamlConfigs) {
        Object port = yamlConfigs.get("port");
        if (port == null) {
            logger.error("[ERROR] yaml.file의 port 라인 읽기 에러");
            throw new IllegalArgumentException();
        }
        this.port = (int) port;
    }

    private void setStaticRoute(Map<String, Object> yamlConfigs) {
        Object staticRoute = yamlConfigs.get("static_route");
        if (staticRoute == null) {
            logger.error("[ERROR] yaml.file의 static_route 라인 읽기 에러");
            throw new IllegalArgumentException();
        }
        this.staticRoute = (String) staticRoute;
    }

    public int getPort() {
        return port;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getStaticRoute() {
        return staticRoute;
    }


}
