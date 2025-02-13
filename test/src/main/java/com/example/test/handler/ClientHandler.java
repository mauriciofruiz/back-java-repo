package com.example.test.handler;

import com.example.test.constants.Constants;
import com.example.test.dto.PersonClientDto;
import com.example.test.exceptions.CustomException;
import com.example.test.service.ClientService;
import com.example.test.service.PersonService;
import com.example.test.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Log4j2
public class ClientHandler {

    private final ClientService clientService;

    private final ObjectValidator objectValidator;

    private Mono<ServerResponse> handleCreateUpdate(Mono<PersonClientDto> clientDto, Function<PersonClientDto, Mono<Void>> operation) {
        return clientDto.flatMap(clientDto1 ->
                operation.apply(clientDto1)
                        .then(Mono.defer(() -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Constants.GENERIC_SUCCESS_MESSAGE)))
                        .onErrorResume(e -> {
                            log.error(Constants.GENERIC_ERROR_MESSAGE_WITH_CAUSE, e.getCause() != null ? e.getCause().getMessage() : e.getMessage(), e);
                            return ServerResponse.badRequest().bodyValue(Constants.GENERIC_ERROR_MESSAGE);
                        }));
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<PersonClientDto> clientDtoMono = request.bodyToMono(PersonClientDto.class)
                .doOnNext(objectValidator::validate)
                .onErrorMap(e -> new CustomException(HttpStatus.BAD_REQUEST, e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        return handleCreateUpdate(clientDtoMono, clientDto -> clientService.create(clientDto).then());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Integer id = Integer.parseInt(request.pathVariable("id"));
        Mono<PersonClientDto> clientDtoMono = request.bodyToMono(PersonClientDto.class)
                .doOnNext(objectValidator::validate)
                .onErrorMap(e -> new CustomException(HttpStatus.BAD_REQUEST, e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        return handleCreateUpdate(clientDtoMono, clientDto -> clientService.update(id, clientDto).then());
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Integer id = Integer.parseInt(request.pathVariable("id"));
        return clientService.getClientById(id)
                .flatMap(person -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(person))
                .switchIfEmpty(ServerResponse.badRequest().bodyValue(Constants.GENERIC_NOT_FOUND_MESSAGE));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Integer id = Integer.parseInt(request.pathVariable("id"));
        return clientService.deleteClientById(id)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Constants.GENERIC_SUCCESS_MESSAGE))
                .onErrorResume(e -> {
                    log.error(Constants.GENERIC_ERROR_MESSAGE_WITH_CAUSE, e.getCause() != null ? e.getCause().getMessage() : e.getMessage(), e);
                    return ServerResponse.badRequest().bodyValue(Constants.GENERIC_ERROR_MESSAGE);
                });
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(clientService.getAllClients(), PersonClientDto.class);
    }
}
