package com.example.testaccount.handler;

import com.example.testaccount.constants.Constants;
import com.example.testaccount.exceptions.CustomException;
import com.example.testaccount.handler.util.ResponseHandlerUtil;
import com.example.testaccount.model.Account;
import com.example.testaccount.service.AccountService;
import com.example.testaccount.validation.ObjectValidator;
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
public class AccountHandler {

    private final AccountService accountService;

    private final ObjectValidator objectValidator;

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Account> accountMono = request.bodyToMono(Account.class)
                .doOnNext(objectValidator::validate)
                .onErrorMap(e -> new CustomException(HttpStatus.BAD_REQUEST, e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        return ResponseHandlerUtil.handleCreateUpdate(accountMono, account -> accountService.save(account).then());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Integer id = Integer.parseInt(request.pathVariable("id"));
        Mono<Account> accountMono = request.bodyToMono(Account.class)
                .doOnNext(objectValidator::validate)
                .onErrorMap(e -> new CustomException(HttpStatus.BAD_REQUEST, e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        return ResponseHandlerUtil.handleCreateUpdate(accountMono, account -> accountService.update(id, account).then());
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        Integer id = Integer.parseInt(request.pathVariable("id"));
        return accountService.getAccountById(id)
                .flatMap(person -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(person))
                .switchIfEmpty(ServerResponse.badRequest().bodyValue(Constants.GENERIC_NOT_FOUND_MESSAGE));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Integer id = Integer.parseInt(request.pathVariable("id"));
        return accountService.deleteAccountById(id)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Constants.GENERIC_SUCCESS_MESSAGE))
                .onErrorResume(e -> {
                    log.error(Constants.GENERIC_ERROR_MESSAGE_WITH_CAUSE, e.getCause() != null ? e.getCause().getMessage() : e.getMessage(), e);
                    return ServerResponse.badRequest().bodyValue(Constants.GENERIC_ERROR_MESSAGE);
                });
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(accountService.getAccounts(), Account.class);
    }
}
