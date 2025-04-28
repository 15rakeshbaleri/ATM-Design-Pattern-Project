package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static Database instance;
    private Connection connection;

    private final String URL = "jdbc:mysql://localhost:3306/atm_db";
    private final String USER = "root";
    private final String PASSWORD = "rakesh@nie2022";

    private Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Make sure you have the JDBC driver
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            connection = null;
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
