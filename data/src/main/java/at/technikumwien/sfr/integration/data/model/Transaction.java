package at.technikumwien.sfr.integration.data.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Builder.Default
    private UUID transactionId = UUID.randomUUID();

    @Builder.Default
    private long timestamp = Instant.now().getEpochSecond();

    private UUID customerId;
    private BigDecimal amount;
    private Currency currency;

}
