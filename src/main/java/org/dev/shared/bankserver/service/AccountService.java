package org.dev.shared.bankserver.service;

import org.dev.shared.bankserver.exception.AccountNotFound;
import org.dev.shared.bankserver.exception.IllegalAccountOperationException;
import org.dev.shared.bankserver.pojo.BankAccount;
import org.dev.shared.bankserver.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalAccountOperationException("Cannot Transfer between the same account");
        }

        BankAccount fromAccount = bankAccountRepository.getBankAccountByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new AccountNotFound("Source account not found"));

        BankAccount toAccount = bankAccountRepository.getBankAccountByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new AccountNotFound("Destination account not found"));


        /* * Lock the involved accounts to prevent concurrent issues.
         * Reentrant locks and Maintaining a lock order is used in order to prevent dead locks are occurring
         * if both the accounts try to transfer funds at the same time
         *  */

        BankAccount firstLock = fromAccountNumber.compareTo(toAccountNumber) < 0 ? fromAccount : toAccount;
        BankAccount secondLock = fromAccountNumber.compareTo(toAccountNumber) < 0 ? toAccount : fromAccount;

        boolean lockedFirstAcc = false;
        boolean lockedSecondAcc = false;

        try {
            lockedFirstAcc = firstLock.getLock().tryLock(300, TimeUnit.MILLISECONDS);
            lockedSecondAcc = secondLock.getLock().tryLock( 300, TimeUnit.MICROSECONDS);

            if (lockedFirstAcc && lockedSecondAcc) {
                fromAccount.withdrawMoney(amount, customerId);
                toAccount.depositMoney(amount);

                bankAccountRepository.save(fromAccount);
                bankAccountRepository.save(toAccount);
            } else {
                throw new IllegalAccountOperationException("Unable to transfer money due to resource limitations");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalAccountOperationException("Unable to transfer money due to resource limitations", e);
        } finally {
            if (lockedFirstAcc) {
                firstLock.getLock().unlock();
            }
            if (lockedSecondAcc) {
                secondLock.getLock().unlock();
            }
        }
    }
}
