package at.technikumwien.sfr.integration.data.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyLaunderingAlert {
    private UUID customerId;
    private UUID transactionId;
    private int violationCode;
}
