package strategy;

import model.Account;

public interface TransactionStrategy {
    boolean execute(Account account, double amount);
}
