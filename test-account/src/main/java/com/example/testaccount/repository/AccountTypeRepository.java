package com.example.testaccount.repository;

import com.example.testaccount.model.AccountType;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends ReactiveCrudRepository<AccountType, Integer> {

}
