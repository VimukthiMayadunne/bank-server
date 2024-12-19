package org.dev.shared.bankserver.service;

import org.dev.shared.bankserver.exception.CustomerNotFound;
import org.dev.shared.bankserver.pojo.Customer;
import org.dev.shared.bankserver.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomerWithAccounts(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFound("Customer not found"));
    }
}
