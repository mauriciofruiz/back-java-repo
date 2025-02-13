package com.example.testaccount.service;

import com.example.testaccount.api.TestApi;
import com.example.testaccount.dto.AccountStatusDto;
import com.example.testaccount.model.Account;
import com.example.testaccount.model.Movement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountStatusService {

    private final TestApi testApi;
    private final AccountService accountService;
    private final AccountTypeService accountTypeService;

    /**
     * Obtiene el estado de la cuenta para un cliente dado.
     *
     * @param movements Lista de movimientos de la cuenta.
     * @param clientId  ID del cliente.
     * @return Un `Flux` de `AccountStatusDto` con el estado de la cuenta.
     */
    public Flux<AccountStatusDto> getAccountStatus(List<Movement> movements, Integer clientId) {
        return testApi.getClienteById(clientId)
                .flatMapMany(client -> {
                    Map<Integer, List<Movement>> groupedMovements = groupMovementsByAccount(movements);

                    return Flux.fromIterable(groupedMovements.entrySet())
                            .flatMapSequential(entry -> {
                                Integer accountMovementId = entry.getKey();
                                List<Movement> movementList = entry.getValue();

                                return accountService.getAccountById(accountMovementId)
                                        .flatMapMany(account -> accountTypeService.getAccountTypeById(account.getAccountTypeId())
                                                .flatMapMany(accountType -> Flux.fromIterable(movementList)
                                                        .map(movement -> createAccountStatusDto(client.getPersonName(), account, accountType.getAccountTypeDescription(), movementList, movement))
                                                )
                                        );
                            });
                });
    }

    /**
     * Agrupa los movimientos por el ID de movimiento de la cuenta.
     *
     * @param movements Lista de movimientos de la cuenta.
     * @return Un mapa donde la clave es el ID de movimiento de la cuenta y el valor es una lista de movimientos.
     */
    private Map<Integer, List<Movement>> groupMovementsByAccount(List<Movement> movements) {
        return movements.stream()
                .collect(Collectors.groupingBy(Movement::getAccountMovementId));
    }

    /**
     * Crea un objeto `AccountStatusDto` con la información del estado de la cuenta.
     *
     * @param clientName             Nombre del cliente.
     * @param account                La cuenta asociada.
     * @param accountTypeDescription Descripción del tipo de cuenta.
     * @param movementList           Lista de movimientos de la cuenta.
     * @param movement               Movimiento específico de la cuenta.
     * @return Un objeto `AccountStatusDto` con la información del estado de la cuenta.
     */
    private AccountStatusDto createAccountStatusDto(String clientName, Account account, String accountTypeDescription, List<Movement> movementList, Movement movement) {
        BigDecimal previousBalance = getPreviousBalance(account, movementList, movement);
        return AccountStatusDto.builder()
                .date(movement.getMovementDate())
                .clientName(clientName)
                .accountNumber(account.getAccountNumber())
                .accountType(accountTypeDescription)
                .initialBalance(previousBalance)
                .movement(movement.getMovementValue())
                .status(account.getAccountStatus())
                .finalBalance(movement.getMovementBalance())
                .build();
    }

    /**
     * Obtiene el saldo anterior de la cuenta.
     *
     * @param account      La cuenta asociada.
     * @param movementList Lista de movimientos de la cuenta.
     * @param movement     Movimiento específico de la cuenta.
     * @return El saldo anterior de la cuenta.
     */
    private BigDecimal getPreviousBalance(Account account, List<Movement> movementList, Movement movement) {
        return movementList.indexOf(movement) == 0 ? account.getInitialBalance() : movementList.get(movementList.indexOf(movement) - 1).getMovementBalance();
    }
}