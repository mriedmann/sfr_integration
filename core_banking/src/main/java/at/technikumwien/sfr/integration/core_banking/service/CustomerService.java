package at.technikumwien.sfr.integration.core_banking.service;

import at.technikumwien.sfr.integration.core_banking.models.CustomerGhost;
import at.technikumwien.sfr.integration.core_banking.repository.CustomerRepository;
import at.technikumwien.sfr.integration.core_banking.streams.CoreBankingStreams;
import at.technikumwien.sfr.integration.data.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.UUID;

@Service
@Slf4j
public class CustomerService {
    private final CoreBankingStreams coreBankingStreams;
    private final CustomerRepository customerRepository;

    public CustomerService(CoreBankingStreams coreBankingStreams, CustomerRepository customerRepository) {
        this.coreBankingStreams = coreBankingStreams;
        this.customerRepository = customerRepository;
    }

    public boolean checkIfCustomerExists(Customer customer) {
        CustomerGhost savedCustomer = customerRepository.getByAttributes(customer);
        return savedCustomer != null;
    }

    public boolean checkIfCustomerExists(UUID customerId) {
        CustomerGhost savedCustomer = customerRepository.getById(customerId);
        return savedCustomer != null;
    }

    public boolean checkIfCustomerIsFrozen(UUID customerId) {
        CustomerGhost savedCustomer = customerRepository.getById(customerId);
        return savedCustomer.isAccountFrozen();
    }

    public void createCustomer(Customer customer) {
        log.info("Sending customer {}", customer);
        MessageChannel messageChannel = coreBankingStreams.outboundCustomers();
        messageChannel.send(MessageBuilder
                .withPayload(customer)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}