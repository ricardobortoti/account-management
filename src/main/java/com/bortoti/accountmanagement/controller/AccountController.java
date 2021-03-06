package com.bortoti.accountmanagement.controller;

import com.bortoti.accountmanagement.domain.Account;
import com.bortoti.accountmanagement.domain.AccountTransfer;
import com.bortoti.accountmanagement.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {
    AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping
    public Account create(@RequestBody Account account) {
        return accountService.create(account);
    }

    @GetMapping("/{accountNumber}")
    public Account getByAccountId(@PathVariable Integer accountNumber) {
        return accountService.findByAccountNumber(accountNumber);
    }

    @GetMapping
    public List<Account> getAll() {
        return accountService.findAll();
    }

    @PostMapping("/{accountNumber}/transactions")
    public AccountTransfer transfer(@PathVariable Integer accountNumber, @RequestBody AccountTransfer accountTransfer){
        accountTransfer.setFrom(accountNumber);
        return accountService.transfer(accountTransfer);
    }
}
