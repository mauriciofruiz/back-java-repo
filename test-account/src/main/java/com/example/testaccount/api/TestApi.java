package com.example.testaccount.api;

import com.example.testaccount.Preference;
import com.example.testaccount.constants.PathConstants;
import com.example.testaccount.dto.PersonClientDtoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TestApi {

    private final WebClient webClient;

    @Autowired
    public TestApi(Preference preference) {
        this.webClient = WebClient.builder()
                .baseUrl(preference.getTestService())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<PersonClientDtoResponse> getClienteById(Integer id) {
        return webClient
                .get()
                .uri(PathConstants.CLIENT_PATH + "/" + id)
                .retrieve()
                .bodyToMono(PersonClientDtoResponse.class);

    }
}
