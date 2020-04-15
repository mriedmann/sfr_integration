package at.technikumwien.sfr.integration.core_banking.repository;

import at.technikumwien.sfr.integration.core_banking.models.CustomerGhost;
import at.technikumwien.sfr.integration.data.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@Repository
public class CustomerRepository {
    private final HashMap<UUID, CustomerGhost> customers = new HashMap<>();
    private final HashMap<Integer, UUID> customerIndex = new HashMap<>();

    public synchronized void addOrUpdate(Customer customer) {
        customers.put(customer.getCustomerId(), new CustomerGhost(customer));
        customerIndex.put(getHash(customer), customer.getCustomerId());
    }

    public CustomerGhost getByAttributes(Customer customer) {
        int hash = getHash(customer);
        return customers.get(customerIndex.get(hash));
    }

    public CustomerGhost getById(UUID customerId) {
        return customers.get(customerId);
    }

    private int getHash(Customer customer) {
        return Objects.hash(customer.getFirstName(), customer.getLastName(), customer.getAddress());
    }
}
