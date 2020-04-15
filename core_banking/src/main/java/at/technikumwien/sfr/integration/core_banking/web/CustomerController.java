package at.technikumwien.sfr.integration.core_banking.web;

import at.technikumwien.sfr.integration.core_banking.service.CustomerService;
import at.technikumwien.sfr.integration.data.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Customer customers(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("address") String address
    ) {
        Customer customer = Customer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .address(address)
                .build();
        if (customerService.checkIfCustomerExists(customer)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer already exists");
        }
        customerService.createCustomer(customer);
        return customer;
    }
}