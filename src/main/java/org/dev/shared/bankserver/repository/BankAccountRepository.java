package org.dev.shared.bankserver.repository;

import org.dev.shared.bankserver.pojo.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> getBankAccountByAccountNumber(String accountNumber);

    Optional<List<BankAccount>> getBankAccountByCusId(Long cusId);

}
