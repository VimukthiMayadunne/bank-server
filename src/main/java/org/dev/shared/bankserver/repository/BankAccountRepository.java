package org.dev.shared.bankserver.repository;

import jakarta.persistence.LockModeType;
import org.dev.shared.bankserver.pojo.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BankAccount> getBankAccountByAccountNumber(@Param("accountNumber") String accountNumber);

    Optional<List<BankAccount>> getBankAccountByCusId(Long cusId);

}
