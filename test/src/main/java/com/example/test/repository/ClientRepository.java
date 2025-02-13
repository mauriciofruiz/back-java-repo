package com.example.test.repository;

import com.example.test.model.Client;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveCrudRepository<Client, Integer> {

    Mono<Client> findClientByPersonClientId(Integer personClientId);
}
