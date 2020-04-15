package at.technikumwien.sfr.integration.money_laundering.service;

import at.technikumwien.sfr.integration.data.model.MoneyLaunderingAlert;
import at.technikumwien.sfr.integration.data.model.Transaction;
import at.technikumwien.sfr.integration.money_laundering.streams.MoneyLaunderingStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MoneyLaunderingProcessor {

    private final MoneyLaunderingEvaluator moneyLaunderingEvaluator;

    public MoneyLaunderingProcessor(MoneyLaunderingEvaluator moneyLaunderingEvaluator) {
        this.moneyLaunderingEvaluator = moneyLaunderingEvaluator;
    }

    @StreamListener(MoneyLaunderingStreams.INPUT_TRANSACTIONS)
    @SendTo(MoneyLaunderingStreams.OUTPUT_MONEYLAUNDERING)
    public MoneyLaunderingAlert handleEvent(@Payload Transaction transaction) {
        log.info("Received transaction event: {}", transaction);
        int violationCode = moneyLaunderingEvaluator.getViolationCode(transaction);
        if (violationCode > 0) {
            MoneyLaunderingAlert alert = MoneyLaunderingAlert.builder()
                    .customerId(transaction.getCustomerId())
                    .transactionId(transaction.getTransactionId())
                    .violationCode(violationCode)
                    .build();
            log.info("Sending money laundering alert event: {}", alert);
            return alert;
        }
        return null;
    }
}
