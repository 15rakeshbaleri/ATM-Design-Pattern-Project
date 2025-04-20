package controller;

import java.util.List;

import dao.AccountDAO;
import model.Account;
import model.Transaction;
import strategy.TransactionStrategy;

public class ATMController {
    private static Account currentAccount;

    public static boolean verify(String cardNumber, String pin) {
        currentAccount = AccountDAO.getAccount(cardNumber);
        return currentAccount != null && AccountDAO.verifyPIN(currentAccount, pin);
    }

    public static boolean performTransaction(String cardNumber, double amount, TransactionStrategy strategy) {
        if (currentAccount == null || !currentAccount.getCardNumber().equals(cardNumber)) {
            currentAccount = AccountDAO.getAccount(cardNumber);
        }
        return strategy.execute(currentAccount, amount);
    }

    public static String getBalance(String cardNumber) {
        if (currentAccount == null || !currentAccount.getCardNumber().equals(cardNumber)) {
            currentAccount = AccountDAO.getAccount(cardNumber);
        }
        double balance = AccountDAO.getBalance(currentAccount);
        return "₹" + String.format("%.2f", balance);
    }

    public static List<Transaction> getTransactions(String cardNumber) {
        Account account = AccountDAO.getAccount(cardNumber);
        return AccountDAO.getTransactions(account);
    }

    public static void resetSession() {
        currentAccount = null;
    }
}
