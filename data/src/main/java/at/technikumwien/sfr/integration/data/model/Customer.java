package at.technikumwien.sfr.integration.data.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Builder.Default
    private UUID customerId = UUID.randomUUID();

    @Builder.Default
    private boolean accountFrozen = false;

    @Builder.Default
    private long timestamp = Instant.now().getEpochSecond();

    private String firstName;
    private String lastName;
    private String address;
}
