package at.technikumwien.sfr.integration.money_laundering.streams;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MoneyLaunderingStreams {
    String INPUT_TRANSACTIONS = "transactions-in";
    String OUTPUT_MONEYLAUNDERING = "moneylaundering-out";

    @Input(INPUT_TRANSACTIONS)
    SubscribableChannel inboundTransactions();

    @Output(OUTPUT_MONEYLAUNDERING)
    MessageChannel outboundMoneyLaundering();
}