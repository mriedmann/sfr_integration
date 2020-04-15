package at.technikumwien.sfr.integration.balance.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
public class Balance {
    private UUID customerId;
    private BigDecimal amount;
    private Currency currency;
}
