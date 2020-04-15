package at.technikumwien.sfr.integration.customer.repository;

import at.technikumwien.sfr.integration.data.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

@Repository
public class CustomerRepository {
    private final HashMap<UUID, Customer> customers = new HashMap<>();

    public Customer getById(UUID customerId) {
        return customers.get(customerId);
    }

    public Collection<Customer> getAll() {
        return customers.values();
    }

    public synchronized void addOrUpdate(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
    }
}
