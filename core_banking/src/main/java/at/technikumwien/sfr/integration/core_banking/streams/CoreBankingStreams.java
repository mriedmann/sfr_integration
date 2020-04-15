package at.technikumwien.sfr.integration.core_banking.streams;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CoreBankingStreams {
    String OUTPUT_TRANSACTIONS = "transactions-out";
    String OUTPUT_CUSTOMERS = "customers-out";
    String INPUT_CUSTOMERS = "customers-in";

    @Output(OUTPUT_TRANSACTIONS)
    MessageChannel outboundTransactions();

    @Output(OUTPUT_CUSTOMERS)
    MessageChannel outboundCustomers();

    @Input(INPUT_CUSTOMERS)
    SubscribableChannel inboundCustomers();
}