package at.technikumwien.sfr.integration.money_laundering;

import at.technikumwien.sfr.integration.data.model.Transaction;

public interface MoneyLaunderingRule {
    @SuppressWarnings("SameReturnValue")
    int getViolationCode();

    boolean isMatch(Transaction transaction);
}
