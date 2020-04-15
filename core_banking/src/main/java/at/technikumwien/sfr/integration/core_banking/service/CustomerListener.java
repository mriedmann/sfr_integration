package at.technikumwien.sfr.integration.core_banking.service;

import at.technikumwien.sfr.integration.core_banking.repository.CustomerRepository;
import at.technikumwien.sfr.integration.core_banking.streams.CoreBankingStreams;
import at.technikumwien.sfr.integration.data.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerListener {
    private final CustomerRepository customerRepository;

    public CustomerListener(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @StreamListener(CoreBankingStreams.INPUT_CUSTOMERS)
    public void handleEvent(@Payload Customer customer) {
        log.info("Received customer event: {}", customer);
        customerRepository.addOrUpdate(customer);
    }
}
