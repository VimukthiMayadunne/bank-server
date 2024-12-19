package org.dev.shared.bankserver.service;

import org.dev.shared.bankserver.exception.AccountNotFound;
import org.dev.shared.bankserver.exception.IllegalAccountOperationException;
import org.dev.shared.bankserver.pojo.BankAccount;
import org.dev.shared.bankserver.repository.BankAccountRepository;
import org.dev.shared.bankserver.util.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTest {

    @MockBean
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private AccountService accountService;

    private BankAccount fromAccount;
    private BankAccount toAccount;

    @BeforeEach
    public void setup() {
        fromAccount = new BankAccount(
                1L,"ACC_12", AccountType.CHECKING,BigDecimal.valueOf(1000),1001L);


        toAccount = new BankAccount(
                2L,"ACC_34", AccountType.CHECKING,BigDecimal.valueOf(1000),1002L);
    }

    @Test
    // This Test will verify transfer service is working accurately
    public void testTransferMoney_Success() {
        when(bankAccountRepository.getBankAccountByAccountNumber("ACC_12"))
                .thenReturn(Optional.of(fromAccount));
        when(bankAccountRepository.getBankAccountByAccountNumber("ACC_34"))
                .thenReturn(Optional.of(toAccount));

        accountService.transferMoney(
                "ACC_12", "ACC_34", BigDecimal.valueOf(200), 1001L);

        verify(bankAccountRepository, times(1)).save(fromAccount);
        verify(bankAccountRepository, times(1)).save(toAccount);

        assert (fromAccount.getBalance().compareTo(BigDecimal.valueOf(800)) == 0);
        assert (toAccount.getBalance().compareTo(BigDecimal.valueOf(1200)) == 0);
    }

    @Test
    // Test to observe how invalid amount is handled
    public void testTransferMoney_InvalidAmount() {
        IllegalAccountOperationException exception = assertThrows(
                IllegalAccountOperationException.class,
                () -> accountService.transferMoney(
                        "ACC_12", "ACC_34", BigDecimal.valueOf(-200), 1001L));

        assert (exception.getMessage().contains("Transfer amount must be greater than zero"));
    }

    @Test
    // Test the behaviour for insufficient balance
    public void testTransferMoney_InsufficientBalance() {
        when(bankAccountRepository.getBankAccountByAccountNumber("ACC_12"))
                .thenReturn(Optional.of(fromAccount));
        when(bankAccountRepository.getBankAccountByAccountNumber("ACC_34"))
                .thenReturn(Optional.of(toAccount));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.transferMoney(
                        "ACC_12", "ACC_34", BigDecimal.valueOf(2000), 1001L));

        assert (exception.getMessage().contains("Insufficient balance"));
    }

    @Test
    // Test the behaviour for not existing  Account
    public void testTransferMoney_AccountNotFound() {
        when(bankAccountRepository.getBankAccountByAccountNumber("123456"))
                .thenReturn(Optional.empty());

        AccountNotFound exception = assertThrows(
                AccountNotFound.class,
                () -> accountService.transferMoney("123456", "654321",
                        BigDecimal.valueOf(200), 1L)
        );
        assert (exception.getMessage().contains("Source account not found"));
    }

    @Test
    // testing validation for customer ID mismatch
    public void testTransferMoney_CustomerIdMismatch() {
        when(bankAccountRepository.getBankAccountByAccountNumber("123456"))
                .thenReturn(Optional.of(fromAccount));
        when(bankAccountRepository.getBankAccountByAccountNumber("654321"))
                .thenReturn(Optional.of(toAccount));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.transferMoney("123456", "654321", BigDecimal.valueOf(100), 99L)
        );

        assert (exception.getMessage().contains("Customer id mismatch"));
    }
}