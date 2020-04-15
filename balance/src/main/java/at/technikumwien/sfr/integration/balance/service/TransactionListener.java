package at.technikumwien.sfr.integration.balance.service;


import at.technikumwien.sfr.integration.balance.repository.TransactionRepository;
import at.technikumwien.sfr.integration.balance.streams.BalanceStreams;
import at.technikumwien.sfr.integration.data.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionListener {

    private final TransactionRepository transactionRepository;

    public TransactionListener(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @StreamListener(BalanceStreams.INPUT)
    public void handleGreetings(@Payload Transaction transaction) {
        log.info("Received transaction: {}", transaction);
        transactionRepository.add(transaction);
    }

}
