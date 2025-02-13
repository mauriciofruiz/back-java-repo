package com.example.test.config;

import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import java.net.URI;

@Configuration
public class R2dbcConfiguration {

    @Value("${spring.r2dbc.url}")
    private String databaseUrl;

    @Value("${spring.r2dbc.username}")
    private String username;

    @Value("${spring.r2dbc.password}")
    private String password;
    @Bean
    public ConnectionFactory connectionFactory() {

        URI uri = URI.create(databaseUrl.replaceAll("r2dbc:postgresql", "https"));

        String host = uri.getHost();
        int port = uri.getPort();
        String database = uri.getPath().substring(1);

        return new PostgresqlConnectionFactory(io.r2dbc.postgresql.PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(port)
                .database(database)
                .username(username)
                .password(password)
                .build());
    }

    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory);
    }
}
