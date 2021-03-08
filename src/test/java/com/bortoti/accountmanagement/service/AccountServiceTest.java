package com.bortoti.accountmanagement.service;

import com.bortoti.accountmanagement.domain.Account;
import com.bortoti.accountmanagement.domain.AccountTransfer;
import com.bortoti.accountmanagement.domain.AccountTransferStatusEnum;
import com.bortoti.accountmanagement.exception.*;
import com.bortoti.accountmanagement.repository.AccountRepository;
import com.bortoti.accountmanagement.repository.AccountTransferRepository;
import com.bortoti.accountmanagement.view.AccountTransferView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {


    @InjectMocks
    AccountService accountService;

    @Mock
    AccountRepository accountRepositoryMock;

    @Mock
    AccountTransferRepository accountTransferRepositoryMock;

    @Captor
    ArgumentCaptor<Account> accountArgumentCaptor;

    @Test
    public void withdraw_MustReturnOk() {
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
    public void withdraw_MustThrowNotEnoughBalanceException() {
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
    public void withdraw_MustThrowTransactionLimitExceededException() {
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
    public void deposit_MustReturnOk() {
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
    public void transfer_MustReturnOk() {
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
    public void transfer_MustReturnTRANSACTION_LIMIT_EXCEEDED() {
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
    public void transfer_MustReturnThrowSameAccountException() {
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
                .toAccount(fromAccountNumber)
                .amount(BigDecimal.valueOf(900.0))
                .createdAt(createdAt)
                .build();

        //then
        try (MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {
            mockedUUID.when(UUID::randomUUID).thenReturn(transferId);
            try (MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
                mockedLocalDateTime.when(LocalDateTime::now).thenReturn(createdAt);
                assertThrows(SameAccountException.class, ()-> accountService.transfer(transfer));
            }
        }
    }

    @Test
    public void transfer_MustReturnNOT_ENOUGH_BALANCE() {
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

    @Test
    public void create_MustReturnOk() {
        Account account = Account.builder()
                .name("account")
                .accountNumber(1)
                .accountBalance(BigDecimal.valueOf(100))
                .build();

        when(accountRepositoryMock.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.empty());
        when(accountRepositoryMock.save(any(Account.class))).thenReturn(account);

        accountService.create(account);

        verify(accountRepositoryMock, times(1)).save(accountArgumentCaptor.capture());
        Account value = accountArgumentCaptor.getValue();
        assertEquals(account.getName(), value.getName());
        assertEquals(account.getAccountNumber(), value.getAccountNumber());
        assertEquals(account.getAccountBalance(), value.getAccountBalance());
        assertNotNull(value.getId());
    }

    @Test
    public void create_MustThrowAccountNumberAlreadyExistsException() {
        Account account = Account.builder()
                .name("account")
                .accountNumber(1)
                .accountBalance(BigDecimal.valueOf(100))
                .build();

        when(accountRepositoryMock.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        assertThrows(AccountNumberAlreadyExistsException.class, () -> accountService.create(account));
    }

    @Test
    public void findAll_MustReturnOk() {
        Account account = Account.builder()
                .name("account")
                .accountNumber(1)
                .accountBalance(BigDecimal.valueOf(100))
                .build();

        when(accountRepositoryMock.findAll()).thenReturn(List.of(account));

        List<Account> all = accountService.findAll();

        assertEquals(account, all.get(0));
        verify(accountRepositoryMock, times(1)).findAll();
    }

    @Test
    public void findByAccountNumber_MustReturnOk() {
        final var accountNumber = new Random().nextInt();
        final var account = buildAccountObject();

        when(accountRepositoryMock.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        final var byAccountNumber = accountService.findByAccountNumber(accountNumber);

        assertEquals(account, byAccountNumber);
        verify(accountRepositoryMock, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    public void findByAccountNumber_MustReturnException() {
        final var accountNumber = new Random().nextInt();

        when(accountRepositoryMock.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.findByAccountNumber(accountNumber));

        verify(accountRepositoryMock, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    public void findTransfersByAccountNumber_shouldReturn() {
        final var spelAwareProxyProjectionFactory = new SpelAwareProxyProjectionFactory();
        final var projection = spelAwareProxyProjectionFactory.createProjection(AccountTransferView.class,
                buildAccountTransfer());
        final var fromAccountNumber = new Random().nextInt();
        final var toAccountNumber = new Random().nextInt();

        when(accountTransferRepositoryMock.findByFromAccountOrToAccountOrderByCreatedAtDesc(fromAccountNumber, toAccountNumber)).thenReturn(List.of(projection));

        final var transfersByAccountNumber = accountService.findTransfersByAccountNumber(fromAccountNumber, toAccountNumber);

        assertEquals(projection, transfersByAccountNumber.get(0));

        verify(accountTransferRepositoryMock, times(1)).findByFromAccountOrToAccountOrderByCreatedAtDesc(fromAccountNumber, toAccountNumber);
    }

    public static AccountTransfer buildAccountTransfer(){
        return AccountTransfer.builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .toAccount(new Random().nextInt())
                .fromAccount(new Random().nextInt())
                .amount(BigDecimal.valueOf(new Random().nextInt()))
                .status(AccountTransferStatusEnum.SUCCESS)
                .build();
    }

    public static Account buildAccountObject() {
        return Account.builder()
                .name("account")
                .accountNumber(1)
                .accountBalance(BigDecimal.valueOf(100))
                .build();
    }
}