package com.bortoti.accountmanagement.controller;

import com.bortoti.accountmanagement.domain.Account;
import com.bortoti.accountmanagement.domain.AccountTransfer;
import com.bortoti.accountmanagement.dto.AccountRequest;
import com.bortoti.accountmanagement.dto.AccountTransferRequest;
import com.bortoti.accountmanagement.service.AccountService;
import com.bortoti.accountmanagement.view.AccountTransferView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
@Api(value = "Accounts")
public class AccountController extends AbstractRestController<Integer> {
    private final AccountService accountService;

    private final ModelMapper modelMapper;

    public AccountController(AccountService accountService, ModelMapper modelMapper){
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ApiOperation(value = "Create a Client Account")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "AccountCreated", response = Account.class),
            @ApiResponse(code = 400, message = "Invalid Account Request")})
    public ResponseEntity<Account> create(@RequestBody @Valid AccountRequest accountRequest) {
        var account = modelMapper.map(accountRequest, Account.class);
        var createdAccount = accountService.create(account);
        return newCreatedResponse(createdAccount.getAccountNumber(),createdAccount);
    }

    @GetMapping("/{accountNumber}")
    @ApiOperation(value = "Gets an Account by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = Account.class),
            @ApiResponse(code = 404, message = "Account Not Found")})
    public ResponseEntity<Account> getByAccountId(@PathVariable Integer accountNumber) {
        var recoveredAccount = accountService.findByAccountNumber(accountNumber);
        return ResponseEntity.ok(recoveredAccount);
    }

    @GetMapping
    @ApiOperation(value = "Gets all Accounts with respective clients")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = Account.class, responseContainer = "List")})
    public ResponseEntity<List<Account>> getAll() {
        var allAccounts = accountService.findAll();
        return ResponseEntity.ok(allAccounts);
    }

    @PostMapping("/{accountNumber}/transfers")
    @ApiOperation(value = "Perform a Transfer to the desired account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = AccountTransfer.class)})
    public ResponseEntity<AccountTransfer> transfer(@PathVariable Integer accountNumber, @Valid @RequestBody AccountTransferRequest accountTransferRequest){
        var accountTransfer = modelMapper.map(accountTransferRequest, AccountTransfer.class);
        accountTransfer.setFromAccount(accountNumber);
        var transfer = accountService.transfer(accountTransfer);
        return ResponseEntity.ok(transfer);
    }

    @GetMapping("/{accountNumber}/transfers")
    @ApiOperation(value = "Gets all transfers from account")
    public ResponseEntity<List<AccountTransferView>> getTransfers(@PathVariable Integer accountNumber){
        var accountTransfers = accountService.findTransfersByAccountNumber(accountNumber, accountNumber);
        return ResponseEntity.ok(accountTransfers);
    }
}
