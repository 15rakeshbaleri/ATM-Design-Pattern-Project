package dao;

import model.Transaction;
import java.sql.*;

public class TransactionDAO {

    public static void logTransaction(Transaction transaction) {
        try {
            Connection conn = Database.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO transactions (account_id, type, amount, timestamp) VALUES (?, ?, ?, ?)");
            stmt.setInt(1, transaction.getAccountId());
            stmt.setString(2, transaction.getType());
            stmt.setDouble(3, transaction.getAmount());
            stmt.setTimestamp(4, Timestamp.valueOf(transaction.getTimestamp()));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
