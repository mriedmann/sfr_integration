package at.technikumwien.sfr.integration.customer.service;

import at.technikumwien.sfr.integration.customer.repository.CustomerRepository;
import at.technikumwien.sfr.integration.data.model.Customer;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomer(UUID customerId) {
        return customerRepository.getById(customerId);
    }

    public Customer getLatestCustomer() {
        return customerRepository
                .getAll().stream()
                .max(Comparator.comparingLong(Customer::getTimestamp))
                .orElseThrow();
    }

    public Collection<Customer> getAllCustomer() {
        return customerRepository
                .getAll();
    }
}
