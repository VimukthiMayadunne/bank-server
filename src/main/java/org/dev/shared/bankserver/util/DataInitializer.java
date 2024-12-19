package org.dev.shared.bankserver.util;

import org.dev.shared.bankserver.pojo.BankAccount;
import org.dev.shared.bankserver.pojo.Customer;
import org.dev.shared.bankserver.repository.BankAccountRepository;
import org.dev.shared.bankserver.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;

    public DataInitializer(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        customerRepository.deleteAll();
        bankAccountRepository.deleteAll();
        customerRepository.save(new Customer(11001L, "user1", "user1@gmail.com"));
        customerRepository.save(new Customer(11002L, "user2", "user2@gmail.com"));
        customerRepository.save(new Customer(11003L, "user3", "user3@gmail.com"));

        bankAccountRepository.save(new BankAccount(22035L, "ACC-22035-user1", AccountType.SAVINGS,
                BigDecimal.valueOf(1000), 11001L));
        bankAccountRepository.save(new BankAccount(22036L, "ACC-22036-user1", AccountType.CHECKING,
                BigDecimal.valueOf(1000), 11002L));
        bankAccountRepository.save(new BankAccount(22037L, "ACC-22037-user1", AccountType.FIXED,
                BigDecimal.valueOf(1000), 11003L));

    }
}
