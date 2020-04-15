package at.technikumwien.sfr.integration.core_banking.service;

import at.technikumwien.sfr.integration.core_banking.streams.CoreBankingStreams;
import at.technikumwien.sfr.integration.data.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
@Slf4j
public class TransactionService {
    private final CoreBankingStreams coreBankingStreams;

    public TransactionService(CoreBankingStreams coreBankingStreams) {
        this.coreBankingStreams = coreBankingStreams;
    }

    public void createTransaction(Transaction transaction) {
        log.info("Sending transaction {}", transaction);
        MessageChannel messageChannel = coreBankingStreams.outboundTransactions();
        messageChannel.send(MessageBuilder
                .withPayload(transaction)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}
