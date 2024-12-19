package org.dev.shared.bankserver.repository;

import org.dev.shared.bankserver.pojo.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
