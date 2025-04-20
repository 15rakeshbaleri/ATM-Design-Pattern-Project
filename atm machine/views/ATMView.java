package views;

import java.awt.*;
import javax.swing.*;
import controller.ATMController;
import model.Transaction;
import strategy.DepositStrategy;
import strategy.WithdrawStrategy;
import java.util.List;
import strategy.TransactionStrategy;

public class ATMView extends JFrame {
    private JPanel container;
    private JTextField cardField;
    private JPasswordField pinField;
    private String currentCard;

    public ATMView() {
        setTitle("ATM Machine");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Main container to hold login and menu panels
        container = new JPanel(new CardLayout());
        container.add(createLoginPanel(), "LOGIN");
        container.add(createMenuPanel(), "MENU");

        add(container);
        setVisible(true);
    }

    // Create login panel with welcome text and input fields
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20, 20, 40));

        // Welcome Panel on the left side
        JPanel welcomePanel = createWelcomePanel();
        welcomePanel.setPreferredSize(new Dimension(400, 0));

        // Form Panel for Card Number and PIN input
        JPanel formPanel = createLoginFormPanel();

        // Keypad for input
        JPanel keypad = createKeypadPanel(cardField);

        panel.add(welcomePanel, BorderLayout.WEST);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(keypad, BorderLayout.SOUTH);

        return panel;
    }

    // Create a welcome panel with a greeting message
    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(20, 20, 40));
        JLabel welcomeLabel = new JLabel("Welcome to ATM Machine");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);
        return welcomePanel;
    }

    // Create the login form with Card Number and PIN fields
    private JPanel createLoginFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 0, 80));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150)); // Adjust padding
        formPanel.setOpaque(false);

        JLabel cardLabel = styleLabel("Card Number:");
        cardField = new JTextField();
        cardField.setFont(new Font("Arial", Font.BOLD, 30));
        cardLabel.setFont(new Font("Arial", Font.BOLD, 30));
        styleField(cardField, 120, 30);

        JLabel pinLabel = styleLabel("PIN:");
        pinField = new JPasswordField();
        pinField.setFont(new Font("Arial", Font.BOLD, 30));
        styleField(pinField, 120, 30);

        JButton loginButton = styleButton("Login");
        loginButton.setPreferredSize(new Dimension(120, 40));

        loginButton.addActionListener(e -> {
            String card = cardField.getText();
            String pin = new String(pinField.getPassword());

            if (ATMController.verify(card, pin)) {
                currentCard = card;
                JOptionPane.showMessageDialog(this, "Login successful");
                ((CardLayout) container.getLayout()).show(container, "MENU");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid card or PIN", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(cardLabel);
        formPanel.add(cardField);
        formPanel.add(pinLabel);
        formPanel.add(pinField);
        formPanel.add(new JLabel()); // Empty space
        formPanel.add(loginButton);

        return formPanel;
    }

    // Create menu panel for ATM operations
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 500, 100, 500));
        panel.setBackground(new Color(30, 30, 60));

        JButton withdrawBtn = styleButton("Withdraw");
        JButton depositBtn = styleButton("Deposit");
        JButton balanceBtn = styleButton("Check Balance");
        JButton transactionsBtn = styleButton("View Transactions");
        JButton exitBtn = styleButton("Exit");

        // Withdraw Button Action
        withdrawBtn.addActionListener(e -> handleWithdraw());

        // Deposit Button Action
        depositBtn.addActionListener(e -> handleDeposit());

        balanceBtn.addActionListener(e -> {
            String balance = ATMController.getBalance(currentCard);
            JOptionPane.showMessageDialog(this, "Your current balance is ₹" + balance);
        });

        transactionsBtn.addActionListener(e -> handleTransactions());

        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(withdrawBtn);
        panel.add(depositBtn);
        panel.add(balanceBtn);
        panel.add(transactionsBtn);
        panel.add(exitBtn);

        return panel;
    }

    // Handle withdrawal
    private void handleWithdraw() {
        String amtStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        try {
            double amount = Double.parseDouble(amtStr);
            TransactionStrategy withdrawStrategy = new WithdrawStrategy();
            if (ATMController.performTransaction(currentCard, amount, withdrawStrategy)) {
                JOptionPane.showMessageDialog(this, "Withdrawal successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient balance or error.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount");
        }
    }

    // Handle deposit
    private void handleDeposit() {
        String amtStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        try {
            double amount = Double.parseDouble(amtStr);
            TransactionStrategy depositStrategy = new DepositStrategy();
            if (ATMController.performTransaction(currentCard, amount, depositStrategy)) {
                JOptionPane.showMessageDialog(this, "Deposit successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Deposit failed.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount");
        }
    }

    // Handle transaction history
    private void handleTransactions() {
        List<Transaction> transactions = ATMController.getTransactions(currentCard);
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions found.");
        } else {
            StringBuilder sb = new StringBuilder("Recent Transactions:\n\n");
            for (Transaction t : transactions) {
                sb.append(String.format("[%s] %s: ₹%.2f\n", t.getTimestamp(), t.getType(), t.getAmount()));
            }
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog(this, scrollPane, "Transaction History", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Keypad for digit input (to be used in login or transaction screens)
    private JPanel createKeypadPanel(JTextField targetField) {
        JPanel keypad = new JPanel(new GridLayout(4, 3, 10, 10));
        keypad.setBorder(BorderFactory.createEmptyBorder(20, 500, 20, 500));
        keypad.setBackground(new Color(20, 20, 40));

        for (int i = 1; i <= 9; i++) {
            final int digit = i;
            JButton btn = styleButton(String.valueOf(digit));
            btn.addActionListener(e -> targetField.setText(targetField.getText() + digit));
            keypad.add(btn);
        }

        keypad.add(new JLabel());
        JButton zeroBtn = styleButton("0");
        zeroBtn.addActionListener(e -> targetField.setText(targetField.getText() + "0"));
        keypad.add(zeroBtn);

        JButton backBtn = styleButton("←");
        backBtn.addActionListener(e -> {
            String text = targetField.getText();
            if (!text.isEmpty()) {
                targetField.setText(text.substring(0, text.length() - 1));
            }
        });
        keypad.add(backBtn);

        return keypad;
    }

    // Helper method to style buttons
    private JButton styleButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        return btn;
    }

    // Helper method to style labels
    private JLabel styleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        return label;
    }

    // Helper method to style text fields
    private void styleField(JTextField field, int width, int height) {
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(width, height)); // Adjusted size
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }

    // Main method to launch the ATM application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMView());
    }
}
