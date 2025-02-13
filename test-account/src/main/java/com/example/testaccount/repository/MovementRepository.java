package com.example.testaccount.repository;

import com.example.testaccount.model.Movement;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface MovementRepository extends ReactiveCrudRepository<Movement, Integer> {

    Mono<Movement> findFirstByAccountMovementIdOrderByMovementDateDesc(Integer accountMovementId);

    @Query("SELECT * FROM test.movimientos m " +
            " LEFT JOIN test.cuenta c on c.id_cuenta = m.id_cuenta_movimiento" +
            " LEFT JOIN test.cliente cli on cli.id_cliente = c.id_cliente_cuenta" +
            " WHERE m.fecha_movimiento BETWEEN :startDate AND :endDate AND cli.id_cliente = :clientId" +
            " order by m.id_cuenta_movimiento, m.fecha_movimiento")
    Flux<Movement> getAccountStatus(LocalDateTime startDate, LocalDateTime endDate, Integer clientId);

}
