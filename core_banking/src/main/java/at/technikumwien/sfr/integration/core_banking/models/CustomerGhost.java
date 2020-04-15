package at.technikumwien.sfr.integration.core_banking.models;

import at.technikumwien.sfr.integration.data.model.Customer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class CustomerGhost {

    private final UUID customerId;
    private boolean accountFrozen;

    public CustomerGhost(Customer customer) {
        this.customerId = customer.getCustomerId();
        this.accountFrozen = customer.isAccountFrozen();
    }
}
