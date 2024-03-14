package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import webserver.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class YalmConfigReader {
    private static final Logger logger = LoggerFactory.getLogger(YalmConfigReader.class);

    public static String loadStaticSourcePathFromYaml() {
        try (InputStream input = new FileInputStream("config.yaml")) {
            Yaml yaml = new Yaml();
            Map<String, String> map = yaml.load(input);
            return map.get("staticSourcePath");
        } catch (IOException e) {
            logger.error("Error loading YAML file", e);
            return "./src/main/resources/static"; // default 리턴 값
        }
    }

    public static int loadPortFromYaml() {
        try (InputStream input = new FileInputStream("config.yaml")) {
            Yaml yaml = new Yaml();
            Map<String, Integer> map = yaml.load(input);
            return map.get("port");
        } catch (IOException e) {
            logger.error("Error loading YAML file", e);
            return 8080; // Default port
        }
    }


}
