package strategy;

import dao.AccountDAO;
import model.Account;

public class WithdrawStrategy implements TransactionStrategy {
    public boolean execute(Account account, double amount) {
        return AccountDAO.withdraw(account, amount);
    }
}
