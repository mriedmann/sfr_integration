package at.technikumwien.sfr.integration.balance.streams;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface BalanceStreams {
    String INPUT = "transactions-in";

    @Input(INPUT)
    SubscribableChannel inboundTransactions();
}