package com.example.testaccount;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

@Log4j2
@Component("preference")
public class Preference implements Serializable {

    private final Properties properties = new Properties();

    private final Environment environment;

    @Autowired
    public Preference(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    private void loadProperties() {
        String activeProfile = environment.getProperty("spring.profiles.active");
        if ("docker".equals(activeProfile)) {
            log.info("*********Container**********");
            loadPropertiesFromResource("/external-service-docker.properties");
        } else {
            log.info("*********Local**********");
            loadPropertiesFromResource("/external-service.properties");
        }
    }

    private void loadPropertiesFromResource(String resourcePath) {
        try (InputStream input = getClass().getResourceAsStream(resourcePath)) {
            if (input != null) {
                log.info("*********External Services Found**********");
                properties.load(input);
            } else {
                log.error("Could not load properties file: {}", resourcePath);
            }
        } catch (IOException e) {
            log.error("Error loading properties file: {}", e.getMessage());
        }
    }

    private String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getTestService() {
        return getProperty("testServiceUrl", null);
    }
}
