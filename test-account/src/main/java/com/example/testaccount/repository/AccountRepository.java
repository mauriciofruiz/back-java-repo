package com.example.testaccount.repository;

import com.example.testaccount.model.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, Integer> {

}
