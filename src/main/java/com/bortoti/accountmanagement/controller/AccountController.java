package com.bortoti.accountmanagement.controller;

import com.bortoti.accountmanagement.domain.Account;
import com.bortoti.accountmanagement.domain.AccountTransfer;
import com.bortoti.accountmanagement.service.AccountService;
import com.bortoti.accountmanagement.view.AccountTransferView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
@Api(value = "Accounts")
public class AccountController {
    AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping
    @ApiOperation(value = "Create a Client Account")
    public Account create(@RequestBody Account account) {
        return accountService.create(account);
    }

    @GetMapping("/{accountNumber}")
    @ApiOperation(value = "Gets an Account by Id")
    public Account getByAccountId(@PathVariable Integer accountNumber) {
        return accountService.findByAccountNumber(accountNumber);
    }

    @GetMapping
    @ApiOperation(value = "Gets all Accounts with respective clients")
    public List<Account> getAll() {
        return accountService.findAll();
    }

    @PostMapping("/{accountNumber}/transfers")
    @ApiOperation(value = "Perform a Transfer to the desired account")
    public AccountTransfer transfer(@PathVariable Integer accountNumber, @RequestBody AccountTransfer accountTransfer){
        accountTransfer.setFromAccount(accountNumber);
        return accountService.transfer(accountTransfer);
    }

    @GetMapping("/{accountNumber}/transfers")
    @ApiOperation(value = "Gets all transfers from account")
    public List<AccountTransferView> getTransfers(@PathVariable Integer accountNumber){
        return accountService.findTransfersByAccountNumber(accountNumber, accountNumber);
    }
}
