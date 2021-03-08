package com.bortoti.accountmanagement.service;

import com.bortoti.accountmanagement.domain.Account;
import com.bortoti.accountmanagement.domain.AccountTransfer;
import com.bortoti.accountmanagement.domain.AccountTransferStatusEnum;
import com.bortoti.accountmanagement.exception.*;
import com.bortoti.accountmanagement.repository.AccountRepository;
import com.bortoti.accountmanagement.repository.AccountTransferRepository;
import com.bortoti.accountmanagement.view.AccountTransferView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        if (accountRepository.findByAccountNumber(account.getAccountNumber()).isPresent()) {
            throw new AccountNumberAlreadyExistsException();
        }

        account.setId(UUID.randomUUID());
        return save(account);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findByAccountNumber(Integer accountNumber) {
        return  accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account Not Found"));
    }

    public void withdraw(Account account, BigDecimal amount) {
        if (account.getAccountBalance().compareTo(amount) < 0) {
            throw new NotEnoughBalanceException();
        }

        if (amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            throw new TransactionLimitExceededException();
        }

        account.setAccountBalance(account.getAccountBalance().subtract(amount));
    }

    public void deposit(Account account, BigDecimal amount) {
        account.setAccountBalance(account.getAccountBalance().add(amount));
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
        accountTransfer.setStatus(AccountTransferStatusEnum.SUCCESS);

        try {
            withdraw(from, accountTransfer.getAmount());
            deposit(to, accountTransfer.getAmount());
            save(from);
            save(to);
        } catch (NotEnoughBalanceException e) {
            accountTransfer.setStatus(AccountTransferStatusEnum.NOT_ENOUGH_BALANCE);
        } catch (TransactionLimitExceededException e) {
            accountTransfer.setStatus(AccountTransferStatusEnum.TRANSACTION_LIMIT_EXCEEDED);
        }



        return accountTransferRepository.save(accountTransfer);
    }

    public List<AccountTransferView> findTransfersByAccountNumber(Integer fromAccount, Integer toAccount) {
        return accountTransferRepository.findByFromAccountOrToAccountOrderByCreatedAtDesc(fromAccount, toAccount);
    }
}
