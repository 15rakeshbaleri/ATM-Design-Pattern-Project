package main;

import java.sql.Connection;

import dao.Database;

public class Main {
    public static void main(String[] args) {
        Connection conn = Database.getInstance().getConnection();
        if (conn == null) {
            System.out.println("❌ Failed to connect to the database.");
        } else {
            System.out.println("✅ Connected to the database!");
        }
    }

}
