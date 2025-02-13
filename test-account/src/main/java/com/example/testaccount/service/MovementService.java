package com.example.testaccount.service;

import com.example.testaccount.constants.Constants;
import com.example.testaccount.dto.AccountStatusDto;
import com.example.testaccount.dto.MovementDto;
import com.example.testaccount.exceptions.CustomException;
import com.example.testaccount.model.Account;
import com.example.testaccount.model.Movement;
import com.example.testaccount.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MovementService {

    private final MovementRepository movementRepository;
    private final AccountService accountService;
    private final AccountStatusService accountStatusService;

    public Mono<Void> update(Integer id, Movement movement) {
        return movementRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, Constants.MOVEMENT_NOT_FOUND_MESSAGE + id)))
                .flatMap(existingMovement -> updateExistingMovement(existingMovement, movement))
                .then();
    }

    public Mono<Movement> getMovementById(Integer id) {
        return movementRepository.findById(id);
    }

    public Flux<Movement> getMovements() {
        return movementRepository.findAll();
    }

    public Mono<Void> deleteMovementById(Integer id) {
        return movementRepository.deleteById(id);
    }

    /**
     * Crea un nuevo movimiento basado en el DTO proporcionado.
     *
     * @param movementDto el DTO del movimiento a crear
     * @return un Mono que indica la finalización de la operación
     */
    public Mono<Void> createMovement(MovementDto movementDto) {
        return getPreviousBalance(movementDto.getAccountMovementId())
                .flatMap(balance -> createAndSaveMovement(movementDto, balance));
    }

    /**
     * Obtiene el estado de la cuenta para un cliente en un rango de fechas.
     *
     * @param startDate la fecha de inicio del rango
     * @param endDate la fecha de fin del rango
     * @param clientId el ID del cliente
     * @return un Flux que emite los estados de la cuenta
     */
    public Flux<AccountStatusDto> getAccountStatus(LocalDateTime startDate, LocalDateTime endDate, Integer clientId) {
        if (startDate == null || endDate == null || clientId == null) {
            return Flux.error(new CustomException(HttpStatus.BAD_REQUEST, Constants.MISSING_PARAMETER_MESSAGE));
        }
        return movementRepository.getAccountStatus(startDate, endDate, clientId)
                .collectList()
                .flatMapMany(movements -> accountStatusService.getAccountStatus(movements, clientId));
    }

    /**
     * Actualiza un movimiento existente con los nuevos datos proporcionados.
     *
     * @param existingMovement el movimiento existente a actualizar
     * @param newMovement los nuevos datos del movimiento
     * @return un Mono que emite el movimiento actualizado
     */
    private Mono<Movement> updateExistingMovement(Movement existingMovement, Movement newMovement) {
        existingMovement.setMovementDate(newMovement.getMovementDate());
        existingMovement.setAccountMovementId(newMovement.getAccountMovementId());
        existingMovement.setMovementValue(newMovement.getMovementValue());
        existingMovement.setMovementBalance(newMovement.getMovementBalance());
        return movementRepository.save(existingMovement);
    }

    /**
     * Obtiene el saldo anterior de una cuenta de movimiento.
     *
     * @param accountMovementId el ID de la cuenta de movimiento
     * @return un Mono que emite el saldo anterior
     */
    private Mono<BigDecimal> getPreviousBalance(Integer accountMovementId) {
        return movementRepository.findFirstByAccountMovementIdOrderByMovementDateDesc(accountMovementId)
                .map(Movement::getMovementBalance)
                .switchIfEmpty(accountService.getAccountById(accountMovementId).map(Account::getInitialBalance));
    }

    /**
     * Crea y guarda un nuevo movimiento basado en el DTO proporcionado y el saldo anterior.
     *
     * @param movementDto el DTO del movimiento a crear
     * @param previousBalance el saldo anterior de la cuenta
     * @return un Mono que indica la finalización de la operación
     */
    private Mono<Void> createAndSaveMovement(MovementDto movementDto, BigDecimal previousBalance) {
        BigDecimal currentBalance = previousBalance.add(movementDto.getMovementValue());

        if (currentBalance.compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, Constants.NO_BALANCE_MESSAGE));
        }

        Movement movement = Movement.builder()
                .movementDate(LocalDateTime.now())
                .accountMovementId(movementDto.getAccountMovementId())
                .movementValue(movementDto.getMovementValue())
                .movementBalance(currentBalance)
                .build();

        return movementRepository.save(movement).then();
    }
}