package at.technikumwien.sfr.integration.money_laundering.service;

import at.technikumwien.sfr.integration.data.model.Transaction;
import at.technikumwien.sfr.integration.money_laundering.MoneyLaunderingRule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoneyLaunderingEvaluator {
    private final List<MoneyLaunderingRule> rules;

    public MoneyLaunderingEvaluator(List<MoneyLaunderingRule> rules) {
        this.rules = rules;
    }

    public int getViolationCode(final Transaction transaction) {
        return rules.parallelStream()
                .filter(rule -> rule.isMatch(transaction))
                .map(MoneyLaunderingRule::getViolationCode)
                .reduce(0, (result, code) -> result | code);
    }
}
