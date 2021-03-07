package com.bortoti.accountmanagement.service;

import com.bortoti.accountmanagement.domain.Account;
import com.bortoti.accountmanagement.domain.AccountTransfer;
import com.bortoti.accountmanagement.exception.AccountNotFoundException;
import com.bortoti.accountmanagement.exception.NotEnoughBalanceException;
import com.bortoti.accountmanagement.exception.SameAccountException;
import com.bortoti.accountmanagement.exception.TransactionLimitExceededException;
import com.bortoti.accountmanagement.repository.AccountRepository;
import com.bortoti.accountmanagement.repository.AccountTransferRepository;
import com.bortoti.accountmanagement.view.AccountTransferView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountTransferRepository accountTransferRepository;

    public AccountService(AccountRepository accountRepository, AccountTransferRepository accountTransferRepository) {
        this.accountRepository = accountRepository;
        this.accountTransferRepository = accountTransferRepository;
    }

    private Account save(Account account){
        return accountRepository.save(account);
    }

    public Account create(Account account) {
        account.setId(UUID.randomUUID());
        return save(account);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findByAccountNumber(Integer accountNumber) {
        return  accountRepository.findByAccountNumber(accountNumber).orElseThrow(AccountNotFoundException.notFound(accountNumber));
    }

    void withdraw(Account account, Double amount) {
        if (account.getAccountBalance() < amount) {
            throw new NotEnoughBalanceException();
        }

        if (amount > 1000) {
            throw new TransactionLimitExceededException();
        }

        account.setAccountBalance(account.getAccountBalance() - amount);
    }

    void deposit(Account account, Double amount) {
        account.setAccountBalance(account.getAccountBalance() + amount);
    }

    @Transactional
    public AccountTransfer transfer(AccountTransfer accountTransfer) {
        if (accountTransfer.getFromAccount().equals(accountTransfer.getToAccount())) {
            throw new SameAccountException();
        }

        Account from = findByAccountNumber(accountTransfer.getFromAccount());
        Account to = findByAccountNumber(accountTransfer.getToAccount());
        accountTransfer.setId(UUID.randomUUID());
        accountTransfer.setCreatedAt(LocalDateTime.now());
        accountTransfer.setSuccess(true);

        try {
            withdraw(from, accountTransfer.getAmount());
            deposit(to, accountTransfer.getAmount());
            save(from);
            save(to);
        } catch (Exception e) {
            accountTransfer.setSuccess(false);
        }



        return accountTransferRepository.save(accountTransfer);
    }

    public List<AccountTransferView> findTransfersByAccountNumber(Integer fromAccount, Integer toAccount) {
        return accountTransferRepository.findByFromAccountOrToAccountOrderByCreatedAtDesc(fromAccount, toAccount);
    }
}
