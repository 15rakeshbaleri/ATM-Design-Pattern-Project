package model;

public class Customer extends User {
    private double balance;
    private String address;
    private String contact;

    public Customer(int id, String username, String password, double balance, String address, String contact) {
        super(id, username, password);
        this.balance = balance;
        this.address = address;
        this.contact = contact;
    }

    public double getBalance() {
        return balance;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", balance=" + balance +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }

}
