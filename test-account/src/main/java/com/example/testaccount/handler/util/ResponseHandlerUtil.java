package com.example.testaccount.handler.util;


import com.example.testaccount.constants.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Log4j2
public class ResponseHandlerUtil {

    public static <T> Mono<ServerResponse> handleCreateUpdate(Mono<T> mono, Function<T, Mono<Void>> operation) {
        return mono.flatMap(item ->
                operation.apply(item)
                        .then(Mono.defer(() -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Constants.GENERIC_SUCCESS_MESSAGE)))
                        .onErrorResume(e -> {
                            String errorMessage = Constants.GENERIC_ERROR_MESSAGE + " " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
                            log.error(errorMessage, e);
                            return ServerResponse.badRequest().bodyValue(errorMessage);
                        }));
    }
}