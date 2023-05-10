package com.workshop1;

import com.workshop1.database.Connector;
import com.workshop1.models.Account;
import com.workshop1.models.Bank;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Workshop1 {

  private static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) throws SQLException {
    Bank bank = new Bank(3);
    while (true) {
      try {
        displayPrompt();

        int choice = scanner.nextInt();

        switch (choice) {
          case 0:
            Connector.getInstance().close();
            System.exit(0);
            break;
          case 1:
            displayAllAccount(bank);
            break;
          case 2:
            System.out.print("Enter name: ");
            String name = scanner.next();
            System.out.print("Enter balance: ");
            int balance = scanner.nextInt();
            addAccount(bank, name, balance);
            break;
          case 3:
            System.out.print("Enter account ID: ");
            int id = scanner.nextInt();
            removeAccount(bank, id);
            break;
          case 4:
            System.out.print("Enter account ID: ");
            id = scanner.nextInt();
            System.out.print("Enter amount: ");
            int amount = scanner.nextInt();
            deposit(bank, id, amount);
            break;
          case 5:
            System.out.print("Enter account ID: ");
            id = scanner.nextInt();
            System.out.print("Enter amount: ");
            amount = scanner.nextInt();
            withdraw(bank, id, amount);
            break;
        }
      } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        break;
      }

      System.out.println("======== PROCESS ENDED ========");
    }
    Connector.getInstance().close();
  }

  static void displayPrompt() {
    System.out.println("1. Display all accounts");
    System.out.println("2. Open new account");
    System.out.println("3. Close existing account");
    System.out.println("4. Deposit");
    System.out.println("5. Withdraw");
    System.out.println("0. Exit");
    System.out.print("Enter your choice: ");
  }

  static void displayAllAccount(Bank bank) {
    ArrayList<Account> accounts = bank.getAccounts();
    System.out.printf("%-10s %-20s %10s\n", "ID", "Name", "Balance");
    for (Account account : accounts) {
      System.out.printf(
        "%-10d %-20s %10d\n",
        account.getId(),
        account.getName(),
        account.getBalance()
      );
    }
  }

  static void addAccount(Bank bank, String name, int balance) {
    try {
      Account account = new Account(name, balance);
      bank.addAccount(account);
      System.out.println("Account created successfully");
      System.out.println("Account ID: " + account.getId());
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  static void removeAccount(Bank bank, int accountId) {
    try {
      bank.removeAccount(accountId);
      System.out.println("Account removed successfully");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  static void deposit(Bank bank, int accountId, int amount) {
    try {
      Account account = bank.getAccountById(accountId);
      account.deposit(amount);
      System.out.println("Deposit successful");
      System.out.println("New balance: " + account.getBalance());
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  static void withdraw(Bank bank, int accountId, int amount) {
    try {
      Account account = bank.getAccountById(accountId);
      account.withdraw(amount);
      System.out.println("Withdraw successful");
      System.out.println("New balance: " + account.getBalance());
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}
