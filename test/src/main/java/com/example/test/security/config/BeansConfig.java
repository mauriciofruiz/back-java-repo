package com.example.test.security.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    /**
     * Bean para configurar los recursos
     * @return WebProperties.Resources
     */
    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }
}
