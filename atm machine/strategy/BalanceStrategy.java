package strategy;

import dao.AccountDAO;
import model.Account;

public class BalanceStrategy implements TransactionStrategy {
    public boolean execute(Account account, double amount) {
        System.out.println("Current balance: ₹" + AccountDAO.getBalance(account));
        return true;
    }
}
