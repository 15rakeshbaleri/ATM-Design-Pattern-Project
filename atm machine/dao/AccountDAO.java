package dao;

import model.Account;
import model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public static Account getAccount(String cardNumber) {
        try {
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM accounts WHERE card_number=?");
            stmt.setString(1, cardNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("card_number"), rs.getString("pin"),
                        rs.getDouble("balance"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verifyPIN(Account account, String enteredPin) {
        return account != null && account.getPin().equals(enteredPin);
    }

    public static boolean withdraw(Account account, double amount) {
        if (account.getBalance() < amount)
            return false;

        try {
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement stmt = conn
                    .prepareStatement("UPDATE accounts SET balance = balance - ? WHERE account_id = ?");
            stmt.setDouble(1, amount);
            stmt.setInt(2, account.getId());
            stmt.executeUpdate();
            TransactionDAO.logTransaction(new Transaction(account.getId(), "WITHDRAW", amount));

            account.setBalance(account.getBalance() - amount);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deposit(Account account, double amount) {
        try {
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement stmt = conn
                    .prepareStatement("UPDATE accounts SET balance = balance + ? WHERE account_id = ?");
            stmt.setDouble(1, amount);
            stmt.setInt(2, account.getId());
            stmt.executeUpdate();
            TransactionDAO.logTransaction(new Transaction(account.getId(), "DEPOSIT", amount));

            account.setBalance(account.getBalance() + amount); // update local object
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static double getBalance(Account account) {
        try {
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM accounts WHERE account_id = ?");
            stmt.setInt(1, account.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                account.setBalance(balance); // sync local balance
                return balance;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static List<Transaction> getTransactions(Account account) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT * FROM transactions WHERE account_id = ? ORDER BY timestamp DESC");
            stmt.setInt(1, account.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction tx = new Transaction(
                        rs.getInt("id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("timestamp").toLocalDateTime());
                transactions.add(tx);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
