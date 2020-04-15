package at.technikumwien.sfr.integration.balance.web;

import at.technikumwien.sfr.integration.balance.model.Balance;
import at.technikumwien.sfr.integration.balance.service.BalanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
public class BalanceController {
    private final BalanceService customerService;

    public BalanceController(BalanceService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/balances/{customerId}")
    public Collection<Balance> getCustomerBalances(
            @PathVariable(value = "customerId") UUID customerId
    ) {
        return customerService.getCustomerBalances(customerId);
    }

    @GetMapping("/balances")
    public Collection<Balance> getAllBalances() {
        return customerService.getAllCustomerBalances();
    }

}