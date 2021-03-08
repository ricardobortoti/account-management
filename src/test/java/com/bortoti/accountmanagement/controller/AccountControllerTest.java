package com.bortoti.accountmanagement.controller;

import com.bortoti.accountmanagement.AccountManagementApplication;
import com.bortoti.accountmanagement.configuration.MapperConfig;
import com.bortoti.accountmanagement.domain.Account;
import com.bortoti.accountmanagement.domain.AccountTransfer;
import com.bortoti.accountmanagement.dto.AccountRequest;
import com.bortoti.accountmanagement.dto.AccountTransferRequest;
import com.bortoti.accountmanagement.service.AccountService;
import com.bortoti.accountmanagement.view.AccountTransferView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.List;

import static com.bortoti.accountmanagement.service.AccountServiceTest.buildAccountObject;
import static com.bortoti.accountmanagement.service.AccountServiceTest.buildAccountTransfer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
@ContextConfiguration(
        classes = { AccountManagementApplication.class, MapperConfig.class})
public class AccountControllerTest extends AbstractRestControllerMVCTest {

    @MockBean
    private AccountService accountServiceMock;

    private MultiValueMap<String, String> params  = new LinkedMultiValueMap<>();

    @BeforeEach
    void setUp() throws Exception {
        super.setup();
    }

    public static AccountRequest buildAccountRequest() {
        return AccountRequest.builder()
                .name("account")
                .accountNumber(1)
                .accountBalance(BigDecimal.valueOf(1000)).build();
    }

    @Test
    void create_ShouldCreateAccount() throws Exception {
        Account account = buildAccountObject();

        when(accountServiceMock.create(any())).thenReturn(account);

        performPost("/v1/accounts", buildAccountRequest(), params).andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(account.getName()))
                .andExpect(jsonPath("accountNumber").value(account.getAccountNumber()))
                .andExpect(jsonPath("accountBalance").value(account.getAccountBalance()));
    }

    @Test
    void create_ShouldNotCreateWhenFieldIsNull() throws Exception {
        final var wrongAccountRequest = buildAccountRequest();
        wrongAccountRequest.setAccountNumber(null);

        performPost("/v1/accounts", wrongAccountRequest, params).andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(400))
                .andExpect(jsonPath("status").value("Bad Request"))
                .andExpect(jsonPath("description").value("Validation Errors"))
                .andExpect(jsonPath("attributes[0].attribute").value("accountNumber"))
                .andExpect(jsonPath("attributes[0].message").value("must not be null"));
    }

    @Test
    void getByAccountId_ShouldGetAccountById() throws Exception {
        Account account = buildAccountObject();

        when(accountServiceMock.findByAccountNumber(account.getAccountNumber())).thenReturn(account);

        performGet("/v1/accounts/{id}", params, account.getAccountNumber())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(account.getName()))
                .andExpect(jsonPath("accountNumber").value(account.getAccountNumber()))
                .andExpect(jsonPath("accountBalance").value(account.getAccountBalance()));
    }

    @Test
    void getAll_ShouldGetAllAccounts() throws Exception {
        Account account = buildAccountObject();

        when(accountServiceMock.findAll()).thenReturn(List.of(account));

        performGet("/v1/accounts", params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(account.getName()))
                .andExpect(jsonPath("$[0].accountNumber").value(account.getAccountNumber()))
                .andExpect(jsonPath("$[0].accountBalance").value(account.getAccountBalance()));
    }

    @Test
    void transfer_ShouldPerformTransfer() throws Exception {
        AccountTransfer transfer = buildAccountTransfer();
        AccountTransferRequest accountTransferRequest = AccountTransferRequest.builder()
                .toAccount(1)
                .amount(BigDecimal.valueOf(100))
                .build();

        when(accountServiceMock.transfer(any())).thenReturn(transfer);

        performPost("/v1/accounts/{accountNumber}/transfers", accountTransferRequest, params, transfer.getFromAccount()).andExpect(status().isOk())
                .andExpect(jsonPath("id").value(transfer.getId().toString()))
                .andExpect(jsonPath("fromAccount").value(transfer.getFromAccount()))
                .andExpect(jsonPath("toAccount").value(transfer.getToAccount()))
                .andExpect(jsonPath("amount").value(transfer.getAmount()))
                .andExpect(jsonPath("status").value(transfer.getStatus().toString()));
    }

    @Test
    void transfer_ShouldReturnExceptionWhenBodyIsInvalid() throws Exception {
        AccountTransferRequest wrongTransferRequest = AccountTransferRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .build();


        performPost("/v1/accounts/{accountNumber}/transfers", wrongTransferRequest, params, 2).andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(400))
                .andExpect(jsonPath("status").value("Bad Request"))
                .andExpect(jsonPath("description").value("Validation Errors"))
                .andExpect(jsonPath("attributes[0].attribute").value("toAccount"))
                .andExpect(jsonPath("attributes[0].message").value("Destiny Account cannot be Null"));
    }

    @Test
    void getTransfers_ShouldReturnAllTransfers() throws Exception {

        final var spelAwareProxyProjectionFactory = new SpelAwareProxyProjectionFactory();
        final var projection = spelAwareProxyProjectionFactory.createProjection(AccountTransferView.class,
                buildAccountTransfer());


        when(accountServiceMock.findTransfersByAccountNumber(1,1) ).thenReturn(List.of(projection));

        performGet("/v1/accounts/{accountNumber}/transfers", params, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(projection.getId().toString()))
                .andExpect(jsonPath("$[0].status").value(projection.getStatus().toString()))
                .andExpect(jsonPath("$[0].toAccount").value(projection.getToAccount()))
                .andExpect(jsonPath("$[0].fromAccount").value(projection.getFromAccount()))
                .andExpect(jsonPath("$[0].amount").value(projection.getAmount()));
    }
}