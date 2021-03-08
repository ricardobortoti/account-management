package com.bortoti.accountmanagement.service;

import com.bortoti.accountmanagement.domain.Account;
import com.bortoti.accountmanagement.domain.AccountTransfer;
import com.bortoti.accountmanagement.domain.AccountTransferStatusEnum;
import com.bortoti.accountmanagement.exception.NotEnoughBalanceException;
import com.bortoti.accountmanagement.exception.TransactionLimitExceededException;
import com.bortoti.accountmanagement.repository.AccountRepository;
import com.bortoti.accountmanagement.repository.AccountTransferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void withdraw_MustReturnOk() {
        //case
        Account targetAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("targetAccount")
                .accountNumber(1)
                .accountBalance(BigDecimal.valueOf(100.00))
                .build();
        //when
        accountService.withdraw(targetAccount, BigDecimal.valueOf(50.0));
        //then
        assertEquals(targetAccount.getAccountBalance(), BigDecimal.valueOf(50.0));
    }

    @Test
    void withdraw_MustThrowNotEnoughBalanceException() {
        //case
        Account targetAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("targetAccount")
                .accountNumber(1)
                .accountBalance(BigDecimal.valueOf(100.00))
                .build();
        //when
        assertThrows(NotEnoughBalanceException.class, () -> accountService.withdraw(targetAccount, BigDecimal.valueOf(150.0)));
    }

    @Test
    void withdraw_MustThrowTransactionLimitExceededException() {
        //case
        Account targetAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("targetAccount")
                .accountNumber(1)
                .accountBalance(BigDecimal.valueOf(5000.00))
                .build();
        //when
        assertThrows(TransactionLimitExceededException.class, () -> accountService.withdraw(targetAccount, BigDecimal.valueOf(1050.0)));
    }

    @Test
    void deposit_MustReturnOk() {
        //case
        Account targetAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("targetAccount")
                .accountNumber(1)
                .accountBalance(BigDecimal.valueOf(100.00))
                .build();
        //when
        accountService.deposit(targetAccount, BigDecimal.valueOf(100.50));
        //then
        assertEquals(targetAccount.getAccountBalance(), BigDecimal.valueOf(200.50));
    }

    @Test
    void transfer_MustReturnOk() {
        //case
        Integer fromAccountNumber = 1;
        Integer toAccountNumber = 2;

        Account fromAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("fromAccount")
                .accountNumber(fromAccountNumber)
                .accountBalance(BigDecimal.valueOf(100.00))
                .build();

        Account toAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("toAccount")
                .accountNumber(toAccountNumber)
                .accountBalance(BigDecimal.valueOf(100.0))
                .build();

        UUID transferId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        AccountTransfer transfer = AccountTransfer.builder()
                .id(transferId)
                .fromAccount(fromAccountNumber)
                .toAccount(toAccountNumber)
                .amount(BigDecimal.valueOf(50.0))
                .createdAt(createdAt)
                .build();

        AccountTransfer expectedTransferResult = AccountTransfer.builder()
                .id(transferId)
                .fromAccount(fromAccountNumber)
                .toAccount(toAccountNumber)
                .amount(BigDecimal.valueOf(50.0))
                .createdAt(createdAt)
                .status(AccountTransferStatusEnum.SUCCESS)
                .build();

        //when
        when(accountRepositoryMock.findByAccountNumber(fromAccountNumber)).thenReturn(Optional.of(fromAccount));
        when(accountRepositoryMock.findByAccountNumber(toAccountNumber)).thenReturn(Optional.of(toAccount));
        when(accountRepositoryMock.save(fromAccount)).thenReturn(fromAccount);
        when(accountRepositoryMock.save(toAccount)).thenReturn(toAccount);
        when(accountTransferRepositoryMock.save(expectedTransferResult)).thenReturn(expectedTransferResult);

        //then
        try (MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {
            mockedUUID.when(UUID::randomUUID).thenReturn(transferId);
            try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
                mockedLocalDateTime.when(LocalDateTime::now).thenReturn(createdAt);
                accountService.transfer(transfer);
            }
        }
    }

    @Test
    void transfer_MustReturnTRANSACTION_LIMIT_EXCEEDED() {
        //case
        Integer fromAccountNumber = 1;
        Integer toAccountNumber = 2;

        Account fromAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("fromAccount")
                .accountNumber(fromAccountNumber)
                .accountBalance(BigDecimal.valueOf(2000.00))
                .build();

        Account toAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("toAccount")
                .accountNumber(toAccountNumber)
                .accountBalance(BigDecimal.valueOf(2000.0))
                .build();

        UUID transferId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        AccountTransfer transfer = AccountTransfer.builder()
                .id(transferId)
                .fromAccount(fromAccountNumber)
                .toAccount(toAccountNumber)
                .amount(BigDecimal.valueOf(1050.0))
                .createdAt(createdAt)
                .build();

        AccountTransfer expectedTransferResult = AccountTransfer.builder()
                .id(transferId)
                .fromAccount(fromAccountNumber)
                .toAccount(toAccountNumber)
                .amount(BigDecimal.valueOf(1050.0))
                .createdAt(createdAt)
                .status(AccountTransferStatusEnum.TRANSACTION_LIMIT_EXCEEDED)
                .build();

        //when
        when(accountRepositoryMock.findByAccountNumber(fromAccountNumber)).thenReturn(Optional.of(fromAccount));
        when(accountRepositoryMock.findByAccountNumber(toAccountNumber)).thenReturn(Optional.of(toAccount));
        when(accountTransferRepositoryMock.save(expectedTransferResult)).thenReturn(expectedTransferResult);

        //then
        try (MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {
            mockedUUID.when(UUID::randomUUID).thenReturn(transferId);
            try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
                mockedLocalDateTime.when(LocalDateTime::now).thenReturn(createdAt);
                accountService.transfer(transfer);
            }
        }
    }

    @Test
    void transfer_MustReturnNOT_ENOUGH_BALANCE() {
        //case
        Integer fromAccountNumber = 1;
        Integer toAccountNumber = 2;

        Account fromAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("fromAccount")
                .accountNumber(fromAccountNumber)
                .accountBalance(BigDecimal.valueOf(100))
                .build();

        Account toAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("toAccount")
                .accountNumber(toAccountNumber)
                .accountBalance(BigDecimal.valueOf(100))
                .build();

        UUID transferId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        AccountTransfer transfer = AccountTransfer.builder()
                .id(transferId)
                .fromAccount(fromAccountNumber)
                .toAccount(toAccountNumber)
                .amount(BigDecimal.valueOf(900.0))
                .createdAt(createdAt)
                .build();

        AccountTransfer expectedTransferResult = AccountTransfer.builder()
                .id(transferId)
                .fromAccount(fromAccountNumber)
                .toAccount(toAccountNumber)
                .amount(BigDecimal.valueOf(900.0))
                .createdAt(createdAt)
                .status(AccountTransferStatusEnum.NOT_ENOUGH_BALANCE)
                .build();

        //when
        when(accountRepositoryMock.findByAccountNumber(fromAccountNumber)).thenReturn(Optional.of(fromAccount));
        when(accountRepositoryMock.findByAccountNumber(toAccountNumber)).thenReturn(Optional.of(toAccount));
        when(accountTransferRepositoryMock.save(expectedTransferResult)).thenReturn(expectedTransferResult);

        //then
        try (MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {
            mockedUUID.when(UUID::randomUUID).thenReturn(transferId);
            try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
                mockedLocalDateTime.when(LocalDateTime::now).thenReturn(createdAt);
                accountService.transfer(transfer);
            }
        }
    }
}