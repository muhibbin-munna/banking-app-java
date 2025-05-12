package com.bankapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class BankingApp {
    private static Scanner scanner = new Scanner(System.in);

    // Main menu
    public static void showMenu() {
        while (true) {
            try {
                System.out.println("\n--- Bank Application ---");
                System.out.println("1. Create Account");
                System.out.println("2. Check Balance");
                System.out.println("3. Deposit Money");
                System.out.println("4. Withdraw Money");
                System.out.println("5. Transfer Money");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        checkBalance();
                        break;
                    case 3:
                        depositMoney();
                        break;
                    case 4:
                        withdrawMoney();
                        break;
                    case 5:
                        transferMoney();
                        break;
                    case 6:
                        System.out.println("Thank you for using the bank application. Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }

            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e);
                scanner.nextLine();  // Clear invalid input
            }
        }

    }

    // Create a new bank account
    public static void createAccount() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter account holder name: ");
        String accountHolderName = scanner.nextLine();
        System.out.print("Enter initial deposit: ");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine();  // Consume newline character

        try (Connection conn = DBUtil.getConnection()) {
            String query = "INSERT INTO BankAccount (account_number, account_holder_name, balance) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, accountNumber);
                stmt.setString(2, accountHolderName);
                stmt.setDouble(3, initialDeposit);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Account created successfully!");
    }

    private static BankAccount findAccount(String accountNumber) {
        return new BankAccount(accountNumber); // Retrieve account from DB
    }

    // Check balance
    public static void checkBalance() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        BankAccount account = findAccount(accountNumber);
        System.out.println("Account Balance: " + account.getBalance());
    }

    // Deposit money
    public static void depositMoney() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // Consume newline character

        BankAccount account = findAccount(accountNumber);
        account.deposit(amount);
    }

    // Withdraw money
    public static void withdrawMoney() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter withdrawal amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // Consume newline character

        BankAccount account = findAccount(accountNumber);
        account.withdraw(amount);
    }

    // Transfer money
    public static void transferMoney() {
        System.out.print("Enter source account number: ");
        String sourceAccountNumber = scanner.nextLine();
        System.out.print("Enter target account number: ");
        String targetAccountNumber = scanner.nextLine();
        System.out.print("Enter transfer amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // Consume newline character

        BankAccount sourceAccount = findAccount(sourceAccountNumber);
        BankAccount targetAccount = findAccount(targetAccountNumber);

        sourceAccount.transfer(targetAccount, amount);
    }

    public static void main(String[] args) {
        showMenu();
    }
}
