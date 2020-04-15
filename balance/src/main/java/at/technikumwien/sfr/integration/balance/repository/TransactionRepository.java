package at.technikumwien.sfr.integration.balance.repository;

import at.technikumwien.sfr.integration.data.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TransactionRepository {
    private final HashMap<UUID, List<Transaction>> customerTransactionsIndex = new HashMap<>();
    private final HashMap<UUID, Transaction> transactions = new HashMap<>();

    public List<Transaction> getAllByCustomerId(UUID customerId) {
        return customerTransactionsIndex.getOrDefault(customerId, new LinkedList<>());
    }

    public synchronized void add(Transaction transaction) {
        List<Transaction> customerTransactionIndexSet = customerTransactionsIndex.getOrDefault(transaction.getCustomerId(),
                new LinkedList<>());
        customerTransactionIndexSet.add(transaction);
        transactions.put(transaction.getTransactionId(), transaction);
        customerTransactionsIndex.put(transaction.getCustomerId(), customerTransactionIndexSet);
    }

    public List<Transaction> getAll() {
        return new ArrayList<>(transactions.values());
    }
}
