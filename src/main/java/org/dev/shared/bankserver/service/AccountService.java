package org.dev.shared.bankserver.service;

import org.dev.shared.bankserver.exception.AccountNotFound;
import org.dev.shared.bankserver.exception.IllegalAccountOperationException;
import org.dev.shared.bankserver.pojo.BankAccount;
import org.dev.shared.bankserver.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final BankAccountRepository bankAccountRepository;

    public AccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public List<BankAccount> getBankAccountsForCustomer(Long customerId) throws AccountNotFound {
        return bankAccountRepository.getBankAccountByCusId(customerId)
                .orElseThrow(() -> new AccountNotFound("Account not found"));
    }

    @Transactional
    public void transferMoney(String fromAccountNumber, String toAccountNumber, BigDecimal amount, Long customerId) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalAccountOperationException("Transfer amount must be greater than zero");
        }

        if(fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalAccountOperationException("Cannot Transfer between the same account");
        }

        BankAccount fromAccount = bankAccountRepository.getBankAccountByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new AccountNotFound("Source account not found"));

        BankAccount toAccount = bankAccountRepository.getBankAccountByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new AccountNotFound("Destination account not found"));
        // Lock the involved accounts to prevent concurrent issues
        synchronized (fromAccount) {
            synchronized (toAccount) {

                fromAccount.withdrawMoney(amount, customerId);
                toAccount.depositMoney(amount);

                bankAccountRepository.save(fromAccount);
                bankAccountRepository.save(toAccount);
            }
        }
    }
}
