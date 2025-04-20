package strategy;

import dao.AccountDAO;
import model.Account;

public class DepositStrategy implements TransactionStrategy {
    public boolean execute(Account account, double amount) {
        return AccountDAO.deposit(account, amount);
    }
}
