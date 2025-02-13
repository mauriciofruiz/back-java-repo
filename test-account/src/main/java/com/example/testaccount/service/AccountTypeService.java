package com.example.testaccount.service;

import com.example.testaccount.model.AccountType;
import com.example.testaccount.repository.AccountTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;

    public Mono<Void> save (AccountType accountType) {
        return accountTypeRepository.save(accountType).then();
    }

    public Mono<AccountType> getAccountTypeById(Integer id) {
        return accountTypeRepository.findById(id);
    }

    public Flux<AccountType> getAllAccountTypes() {
        return accountTypeRepository.findAll();
    }
}
