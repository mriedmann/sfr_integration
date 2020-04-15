package at.technikumwien.sfr.integration.core_banking.web;

import at.technikumwien.sfr.integration.core_banking.service.CustomerService;
import at.technikumwien.sfr.integration.core_banking.service.TransactionService;
import at.technikumwien.sfr.integration.data.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@RestController
public class TransactionController {
    private final TransactionService transactionService;
    private final CustomerService customerService;

    public TransactionController(TransactionService transactionService, CustomerService customerService) {
        this.transactionService = transactionService;
        this.customerService = customerService;
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Transaction transactions(
            @RequestParam("customerId") UUID customerId,
            @RequestParam("currency") Currency currency,
            @RequestParam("amount") BigDecimal amount
    ) {
        if (!customerService.checkIfCustomerExists(customerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer does not exist");
        }
        if (customerService.checkIfCustomerIsFrozen(customerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customer account is frozen");
        }
        Transaction transaction = Transaction.builder()
                .customerId(customerId)
                .currency(currency)
                .amount(amount)
                .build();
        transactionService.createTransaction(transaction);
        return transaction;
    }
}