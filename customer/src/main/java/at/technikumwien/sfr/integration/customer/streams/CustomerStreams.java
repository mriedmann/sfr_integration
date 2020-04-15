package at.technikumwien.sfr.integration.customer.streams;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CustomerStreams {
    String INPUT_CUSTOMERS = "customers-in";
    String INPUT_MONEYLAUNDERING = "moneylaundering-in";

    @Input(INPUT_CUSTOMERS)
    SubscribableChannel inboundCustomers();

    @Input(INPUT_MONEYLAUNDERING)
    SubscribableChannel inboundMoneyLaundering();
}