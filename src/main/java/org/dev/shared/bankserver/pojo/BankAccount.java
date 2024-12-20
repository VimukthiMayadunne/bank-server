package org.dev.shared.bankserver.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import org.dev.shared.bankserver.util.AccountType;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;


@Entity
public class BankAccount {
    @Id
    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private Long cusId;

    @Transient
    private final transient ReentrantLock lock = new ReentrantLock();

    public ReentrantLock getLock() {
        return lock;
    }

    public BankAccount(Long id, String accountNumber, AccountType accountType, BigDecimal balance, Long cusId) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.cusId = cusId;
    }

    public BankAccount() {
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Long getCusId() {
        return cusId;
    }

    public synchronized void depositMoney(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        balance = balance.add(amount);
    }

    public synchronized void withdrawMoney(BigDecimal amount, long customerId) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        if (accountType.equals(AccountType.FIXED) || accountType.equals(AccountType.MINOR)) {
            throw new IllegalArgumentException("Withdrawal is not supported");
        }
        if (customerId != this.cusId) {
            throw new IllegalArgumentException("Customer id mismatch");
        }
        balance = balance.subtract(amount);
    }
}
