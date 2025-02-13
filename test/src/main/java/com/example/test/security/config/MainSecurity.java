package com.example.test.security.config;

import com.example.test.security.repository.SecurityContextRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@Log4j2
public class MainSecurity {

    private final SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec
                                .pathMatchers("/").permitAll()
                                .anyExchange().authenticated()
                )
                .securityContextRepository(securityContextRepository)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

    /**
     * Configuración de CORS
     *
     * @return CorsConfigurationSource
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        configuration.setAllowCredentials(Boolean.TRUE);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
