// ATMView.java (Styled Fullscreen ATM Swing Interface)
package views;

import java.util.List;
import controller.ATMController;
import model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ATMView extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;
    private JTextField cardField;
    private JPasswordField pinField;
    private String currentCard;

    public ATMView() {
        setTitle("ATM Machine");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        container.add(createLoginPanel(), "LOGIN");
        container.add(createMenuPanel(), "MENU");

        add(container);
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 60));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 300, 50, 300));
        formPanel.setOpaque(false);

        JLabel cardLabel = styleLabel("Card Number:");
        cardField = new JTextField();
        styleField(cardField);

        JLabel pinLabel = styleLabel("PIN:");
        pinField = new JPasswordField();
        styleField(pinField);

        JButton loginButton = styleButton("Login");

        loginButton.addActionListener(e -> {
            String card = cardField.getText();
            String pin = new String(pinField.getPassword());

            if (ATMController.verify(card, pin)) {
                currentCard = card;
                JOptionPane.showMessageDialog(this, "Login successful");
                cardLayout.show(container, "MENU");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid card or PIN", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(cardLabel);
        formPanel.add(cardField);
        formPanel.add(pinLabel);
        formPanel.add(pinField);
        formPanel.add(new JLabel());
        formPanel.add(loginButton);

        JPanel keypad = createKeypadPanel(cardField, false);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(keypad, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 500, 100, 500));
        panel.setBackground(new Color(25, 25, 50));

        JButton withdrawBtn = styleButton("Withdraw");
        JButton depositBtn = styleButton("Deposit");
        JButton balanceBtn = styleButton("Check Balance");
        JButton transactionsBtn = styleButton("View Transactions");
        JButton exitBtn = styleButton("Exit");

        withdrawBtn.addActionListener(e -> {
            String amtStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
            try {
                double amount = Double.parseDouble(amtStr);
                if (ATMController.withdraw(currentCard, amount)) {
                    JOptionPane.showMessageDialog(this, "Withdrawal successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient balance or error.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount");
            }
        });

        depositBtn.addActionListener(e -> {
            String amtStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
            try {
                double amount = Double.parseDouble(amtStr);
                if (ATMController.deposit(currentCard, amount)) {
                    JOptionPane.showMessageDialog(this, "Deposit successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Deposit failed.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount");
            }
        });

        balanceBtn.addActionListener(e -> {
            String balance = ATMController.getBalance(currentCard);
            JOptionPane.showMessageDialog(this, "Your current balance is ₹" + balance);
        });

        transactionsBtn.addActionListener(e -> {
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

                int choice = JOptionPane.showConfirmDialog(this, "Do you want to download this as a PDF?",
                        "Download PDF", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setSelectedFile(new java.io.File("transactions.pdf"));
                    int userSelection = fileChooser.showSaveDialog(this);

                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        java.io.File fileToSave = fileChooser.getSelectedFile();
                        try {
                            generateTransactionPDF(transactions, fileToSave.getAbsolutePath());
                            JOptionPane.showMessageDialog(this, "PDF saved successfully!");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Error saving PDF: " + ex.getMessage());
                        }
                    }
                }
            }
        });

        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(withdrawBtn);
        panel.add(depositBtn);
        panel.add(balanceBtn);
        panel.add(transactionsBtn);
        panel.add(exitBtn);

        return panel;
    }

    private JPanel createKeypadPanel(JTextField targetField, boolean isPassword) {
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

    private void generateTransactionPDF(List<Transaction> transactions, String filePath) throws Exception {
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(filePath));
        document.open();
        document.add(new com.itextpdf.text.Paragraph("Transaction History\n\n"));

        for (Transaction t : transactions) {
            String line = String.format("[%s] %s: ₹%.2f", t.getTimestamp(), t.getType(), t.getAmount());
            document.add(new com.itextpdf.text.Paragraph(line));
        }

        document.close();
    }

    // UI Helper Methods
    private JButton styleButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setFocusPainted(false);
        return btn;
    }

    private JLabel styleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMView());
    }
}
