package com.bortoti.accountmanagement.service;

import com.bortoti.accountmanagement.domain.Account;
import com.bortoti.accountmanagement.domain.AccountTransfer;
import com.bortoti.accountmanagement.exception.AccountNotFoundException;
import com.bortoti.accountmanagement.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private Account save(Account account){
        return accountRepository.save(account);
    }

    public Account create(Account account) {
        account.setId(UUID.randomUUID());
        return save(account);
    }

    public Account update(Account account){
        return save(account);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findByAccountNumber(Integer accountNumber) {
        return  accountRepository.findByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException.notFound(accountNumber));
    }

    public AccountTransfer transfer(AccountTransfer accountTransfer) {
        return new AccountTransfer();
    }
}
