package at.technikumwien.sfr.integration.customer.web;

import at.technikumwien.sfr.integration.customer.service.CustomerService;
import at.technikumwien.sfr.integration.data.model.Customer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers/{customerId}")
    public Customer getCustomer(
            @PathVariable(value = "customerId") UUID customerId
    ) {
        return customerService.getCustomer(customerId);
    }

    @GetMapping("/customers/latest")
    public Customer getLatestCustomer() {
        return customerService.getLatestCustomer();
    }

    @GetMapping("/customers")
    public Collection<Customer> getAllCustomer() {
        return customerService.getAllCustomer();
    }

}