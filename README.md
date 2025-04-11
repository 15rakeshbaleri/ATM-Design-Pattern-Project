# 💳 ATM Machine - Design Pattern Project

This is a simple ATM system implemented in Java using Swing for the UI, MySQL, and follows the MVC and DAO design patterns. It supports basic banking features like login, balance inquiry, deposit, withdrawal, and transaction history export as PDF using iText.

---

## 🔧 Requirements

- **Java 8 or above**
- **MySQL Database**
- **External JARs**:
    - [iText 5.x.x](https://github.com/itext/itextpdf/releases) – for PDF generation
    - [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) – for JDBC connectivity

---

## ⚙️ Setup Instructions

### 1. Configure Database Connection

Update your MySQL username and password in `Database.java`:

```java
private static final String USER = "root";
private static final String PASSWORD = "your_mysql_password";
```

### 2. Create Tables

Run the following SQL queries in your MySQL environment to create the necessary tables:

```sql
-- Create the database
CREATE DATABASE atm_db;

-- Select the database
USE atm_db;

-- Create users table
CREATE TABLE users (
        id INT PRIMARY KEY AUTO_INCREMENT,
        username VARCHAR(50) UNIQUE NOT NULL,
        password VARCHAR(100) NOT NULL,
        role ENUM('CUSTOMER', 'EMPLOYEE') NOT NULL
);

-- Create accounts table
CREATE TABLE accounts (
        account_id INT PRIMARY KEY AUTO_INCREMENT,
        user_id INT,
        card_number VARCHAR(20) UNIQUE NOT NULL,
        pin VARCHAR(10) NOT NULL,
        balance DOUBLE DEFAULT 0,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create customers table
CREATE TABLE customers (
        id INT PRIMARY KEY AUTO_INCREMENT,
        name VARCHAR(100),
        address VARCHAR(255),
        contact VARCHAR(20),
        username VARCHAR(50) UNIQUE,
        password VARCHAR(100)
);

-- Create transactions table
CREATE TABLE transactions (
        id INT PRIMARY KEY AUTO_INCREMENT,
        account_id INT,
        type VARCHAR(20),
        amount DOUBLE,
        timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);
```

---

## 🚀 Run the Application

1. Open the project in your IDE (e.g., IntelliJ, Eclipse, VS Code).
2. Ensure the external JARs are added to the project classpath.
3. Navigate to the `views` package.
4. Run the `main()` method in `ATMView.java`.

---
