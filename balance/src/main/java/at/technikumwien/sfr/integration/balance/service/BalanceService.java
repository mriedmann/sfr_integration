package at.technikumwien.sfr.integration.balance.service;

import at.technikumwien.sfr.integration.balance.model.Balance;
import at.technikumwien.sfr.integration.balance.repository.TransactionRepository;
import at.technikumwien.sfr.integration.data.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BalanceService {

    private final TransactionRepository transactionRepository;

    public BalanceService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Collection<Balance> getCustomerBalances(UUID customerId) {
        List<Transaction> customerTransactions = transactionRepository.getAllByCustomerId(customerId);
        return getBalances(customerId, customerTransactions);
    }

    public Collection<Balance> getAllCustomerBalances() {
        List<Transaction> transactions = transactionRepository.getAll();
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCustomerId))
                .entrySet().stream()
                .map(x -> getBalances(x.getKey(), x.getValue()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<Balance> getBalances(UUID customerId, List<Transaction> customerTransactions) {
        return customerTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCurrency))
                .entrySet().stream()
                .map(x -> {
                    BigDecimal amount = x.getValue().stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return Balance.builder()
                            .customerId(customerId)
                            .amount(amount)
                            .currency(x.getKey())
                            .build();
                }).collect(Collectors.toList());
    }

}
