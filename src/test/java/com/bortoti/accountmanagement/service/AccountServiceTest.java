package com.bortoti.accountmanagement.service;

import com.bortoti.accountmanagement.domain.Account;
import com.bortoti.accountmanagement.domain.AccountTransfer;
import com.bortoti.accountmanagement.repository.AccountRepository;
import com.bortoti.accountmanagement.repository.AccountTransferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {


    @InjectMocks
    AccountService accountService;

    @Mock
    AccountRepository accountRepositoryMock;

    @Mock
    AccountTransferRepository accountTransferRepositoryMock;

    @Test
    void withdraw() {
        //case
        Account targetAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("targetAccount")
                .accountNumber(1)
                .accountBalance(100.00)
                .build();
        //when
        accountService.withdraw(targetAccount, 50.0);
        //then
        assertEquals(targetAccount.getAccountBalance(), 50.0);
    }

    @Test
    void deposit() {
        //case
        Account targetAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("targetAccount")
                .accountNumber(1)
                .accountBalance(100.00)
                .build();
        //when
        accountService.deposit(targetAccount, 100.50);
        //then
        assertEquals(targetAccount.getAccountBalance(), 200.50);
    }

    @Test
    void transfer() {
        //case
        Integer fromAccountNumber = 1;
        Integer toAccountNumber = 2;

        Account fromAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("fromAccount")
                .accountNumber(fromAccountNumber)
                .accountBalance(100.00)
                .build();

        Account toAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("toAccount")
                .accountNumber(toAccountNumber)
                .accountBalance(100.00)
                .build();

        UUID transferid = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        AccountTransfer transfer = AccountTransfer.builder()
                .id(transferid)
                .fromAccount(fromAccountNumber)
                .toAccount(toAccountNumber)
                .amount(50.0)
                .createdAt(createdAt)
                .build();

        AccountTransfer expectedTransferResult = AccountTransfer.builder()
                .id(transferid)
                .fromAccount(fromAccountNumber)
                .toAccount(toAccountNumber)
                .amount(50.0)
                .createdAt(createdAt)
                .success(true)
                .build();

        //when
        when(accountRepositoryMock.findByAccountNumber(fromAccountNumber)).thenReturn(Optional.of(fromAccount));
        when(accountRepositoryMock.findByAccountNumber(toAccountNumber)).thenReturn(Optional.of(toAccount));
        when(accountRepositoryMock.save(fromAccount)).thenReturn(fromAccount);
        when(accountRepositoryMock.save(toAccount)).thenReturn(toAccount);
        when(accountTransferRepositoryMock.save(expectedTransferResult)).thenReturn(expectedTransferResult);

        //then
        try (MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {
            mockedUUID.when(UUID::randomUUID).thenReturn(transferid);
            try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
                mockedLocalDateTime.when(LocalDateTime::now).thenReturn(createdAt);
                accountService.transfer(transfer);
            }
        }
    }
}