package at.technikumwien.sfr.integration.customer.service;

import at.technikumwien.sfr.integration.customer.repository.CustomerRepository;
import at.technikumwien.sfr.integration.customer.streams.CustomerStreams;
import at.technikumwien.sfr.integration.data.model.Customer;
import at.technikumwien.sfr.integration.data.model.MoneyLaunderingAlert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MoneyLaunderingListener {

    private final CustomerRepository customerRepository;

    public MoneyLaunderingListener(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @StreamListener(CustomerStreams.INPUT_MONEYLAUNDERING)
    public void handleEvent(@Payload MoneyLaunderingAlert moneyLaunderingAlert) {
        log.info("Received money laundering alert event: {}", moneyLaunderingAlert);
        Customer customer = customerRepository.getById(moneyLaunderingAlert.getCustomerId());
        if (customer == null) return;
        customer.setAccountFrozen(true);
        customerRepository.addOrUpdate(customer);
    }
}
