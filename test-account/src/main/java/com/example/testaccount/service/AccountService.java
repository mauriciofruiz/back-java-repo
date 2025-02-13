package com.example.testaccount.service;

import com.example.testaccount.model.Account;
import com.example.testaccount.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Mono<Void> save (Account account) {
        return accountRepository.save(account).then();
    }

    /**
     * Actualiza el estado de una cuenta existente.
     *
     * @param id el ID de la cuenta a actualizar
     * @param account el objeto Account con el nuevo estado
     * @return un Mono<Void> que indica la finalización de la operación
     */
    public Mono<Void> update (Integer id, Account account) {
        return accountRepository.findById(id)
                .flatMap(account1 -> {
                    account1.setAccountStatus(account.getAccountStatus());
                    return accountRepository.save(account1);
                }).then();
    }

    public Mono<Account> getAccountById(Integer id) {
        return accountRepository.findById(id);
    }

    public Mono<Void> deleteAccountById(Integer id) {
        return accountRepository.deleteById(id);
    }

    public Flux<Account> getAccounts() {
        return accountRepository.findAll();
    }


}
