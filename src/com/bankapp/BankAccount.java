package com.bankapp;

import java.sql.*;

public class BankAccount {
    private String accountNumber;
    private String accountHolderName;
    private double balance;

    // Constructor
    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
        loadAccountDetails();
    }

    // Load account details from DB
    private void loadAccountDetails() {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM BankAccount WHERE account_number = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, accountNumber);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    this.accountHolderName = rs.getString("account_holder_name");
                    this.balance = rs.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    // Deposit money using procedure
    public void deposit(double amount) {
        if (amount > 0) {
            try (Connection connection = DBUtil.getConnection();
                 CallableStatement stmt = connection.prepareCall("{call deposit(?,?)}");
            ) {
                stmt.setString(1, accountNumber);
                stmt.setDouble(2, amount);
                stmt.execute();
                System.out.println("Deposited: " + amount);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Withdraw money
    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            updateBalance();
            System.out.println("Withdrew: " + amount + " | New Balance: " + balance);
        } else {
            System.out.println("Insufficient balance or invalid withdrawal amount.");
        }
    }

    // Update balance in DB
    private void updateBalance() {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "UPDATE BankAccount SET balance = ? WHERE account_number = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setDouble(1, balance);
                stmt.setString(2, accountNumber);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Transfer money between accounts
    public void transfer(BankAccount targetAccount, double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            targetAccount.deposit(amount); // Update target account's balance
            updateBalance(); // Update source account's balance
            System.out.println("Transferred: " + amount + " to " + targetAccount.getAccountNumber());
        } else {
            System.out.println("Insufficient balance or invalid transfer amount.");
        }
    }
}
