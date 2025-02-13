package com.example.testaccount.handler;

import com.example.testaccount.constants.Constants;
import com.example.testaccount.dto.AccountStatusDto;
import com.example.testaccount.dto.MovementDto;
import com.example.testaccount.exceptions.CustomException;
import com.example.testaccount.handler.util.ResponseHandlerUtil;
import com.example.testaccount.model.Movement;
import com.example.testaccount.service.MovementService;
import com.example.testaccount.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Log4j2
public class MovementHandler {

    private final MovementService movementService;

    private final ObjectValidator objectValidator;

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<MovementDto> responseMono = request.bodyToMono(MovementDto.class)
                .doOnNext(objectValidator::validate)
                .onErrorMap(e -> new CustomException(HttpStatus.BAD_REQUEST, e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        return ResponseHandlerUtil.handleCreateUpdate(responseMono, movement -> movementService.createMovement(movement).then());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Integer id = Integer.parseInt(request.pathVariable("id"));
        Mono<Movement> movementMono = request.bodyToMono(Movement.class)
                .doOnNext(objectValidator::validate)
                .onErrorMap(e -> new CustomException(HttpStatus.BAD_REQUEST, e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        return ResponseHandlerUtil.handleCreateUpdate(movementMono, movement -> movementService.update(id, movement).then());
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Integer id = Integer.parseInt(request.pathVariable("id"));
        return movementService.getMovementById(id)
                .flatMap(person -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(person))
                .switchIfEmpty(ServerResponse.badRequest().bodyValue(Constants.GENERIC_NOT_FOUND_MESSAGE));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Integer id = Integer.parseInt(request.pathVariable("id"));
        return movementService.deleteMovementById(id)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Constants.GENERIC_SUCCESS_MESSAGE))
                .onErrorResume(e -> {
                    log.error(Constants.GENERIC_ERROR_MESSAGE_WITH_CAUSE, e.getCause() != null ? e.getCause().getMessage() : e.getMessage(), e);
                    return ServerResponse.badRequest().bodyValue(Constants.GENERIC_ERROR_MESSAGE);
                });
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(movementService.getMovements(), Movement.class);
    }

    public Mono<ServerResponse> getAccountStatus(ServerRequest request) {
        String startDateString = request.queryParam("startDate").orElse(null);
        LocalDate startDate;
        LocalDateTime startDateTime = null;
        if (startDateString != null && !startDateString.isEmpty()) {
            startDate = LocalDate.parse(startDateString);
            startDateTime = startDate.atStartOfDay();
        }
        String endDateString = request.queryParam("endDate").orElse(null);
        LocalDate endDate;
        LocalDateTime endDateTime = null;
        if (endDateString != null && !endDateString.isEmpty()) {
            endDate = LocalDate.parse(endDateString);
            endDateTime = endDate.atTime(23, 59, 59);
        }
        Integer clientId = request.queryParam("clientId").map(Integer::parseInt).orElse(null);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(movementService.getAccountStatus(startDateTime, endDateTime, clientId).onErrorMap(e -> {
                            log.error(e.getMessage());
                            return new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
                        }),
                        AccountStatusDto.class);
    }
}
