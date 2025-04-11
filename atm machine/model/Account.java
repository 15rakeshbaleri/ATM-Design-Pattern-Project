package model;

public class Account {
  private int id;
  private String cardNumber;
  private String pin;
  private double balance;

  public Account(int id, String cardNumber, String pin, double balance) {
    this.id = id;
    this.cardNumber = cardNumber;
    this.pin = pin;
    this.balance = balance;
  }

  public int getId() {
    return id;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public String getPin() {
    return pin;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  @Override
  public String toString() {
    return "Account{" +
        "id=" + id +
        ", cardNumber='" + cardNumber + '\'' +
        ", pin='" + pin + '\'' +
        ", balance=" + balance +
        '}';
  }
}
