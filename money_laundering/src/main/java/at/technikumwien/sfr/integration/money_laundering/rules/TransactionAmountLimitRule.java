package at.technikumwien.sfr.integration.money_laundering.rules;

import at.technikumwien.sfr.integration.data.model.Transaction;
import at.technikumwien.sfr.integration.money_laundering.MoneyLaunderingRule;
import org.springframework.stereotype.Component;

@Component
public class TransactionAmountLimitRule implements MoneyLaunderingRule {

    @Override
    public int getViolationCode() {
        return 16;
    }

    @Override
    public boolean isMatch(Transaction transaction) {
        return transaction.getAmount().abs().intValue() > 80000;
    }
}
