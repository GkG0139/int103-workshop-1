package com.workshop1.models;

import com.workshop1.database.Connector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Bank {

  private int id = Integer.MIN_VALUE;
  private String name;

  private ArrayList<Account> accounts = new ArrayList<>();

  public Bank(String name) {
    this.name = name;

    try {
      Connector connector = Connector.getInstance();

      String query = "SELECT * FROM banks WHERE name = '" + name + "'";
      ResultSet resultSet = connector.executeQuery(query);
      if (resultSet.next()) {
        throw new IllegalArgumentException("Bank name already exists");
      }
      query = "INSERT INTO banks (name) VALUES ('" + name + "')";
      connector.executeUpdate(query);

      initialize();
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  public Bank(int id) {
    this.id = id;

    try {
      initialize();
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private void initialize() {
    try {
      Connector connector = Connector.getInstance();
      String query;
      if (id == Integer.MIN_VALUE) {
        query = "SELECT * FROM banks WHERE name = '" + name + "'";
      } else {
        query = "SELECT * FROM banks WHERE id = " + id;
      }
      ResultSet resultSet = connector.executeQuery(query);
      if (resultSet.next()) {
        id = resultSet.getInt("id");
        name = resultSet.getString("name");
      }
      query = "SELECT * FROM accounts WHERE bank_id = " + id;
      resultSet = connector.executeQuery(query);
      while (resultSet.next()) {
        int accountId = resultSet.getInt("id");
        String accountName = resultSet.getString("name");
        int balance = resultSet.getInt("balance");
        Account account = new Account(accountId, accountName, balance);
        accounts.add(account);
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  public void addAccount(Account account) throws Exception {
    try {
      Connector connector = Connector.getInstance();
      PreparedStatement statement = connector.prepareStatement(
        "INSERT INTO accounts (id, name, balance, bank_id) VALUES (?, ?, ?, ?)"
      );
      statement.setInt(1, account.getId());
      statement.setString(2, account.getName());
      statement.setInt(3, account.getBalance());
      statement.setInt(4, id);
      statement.executeUpdate();
      accounts.add(account);
    } catch (Exception e) {
      throw new Exception(e.getMessage(), e.getCause());
    }
  }

  public void removeAccount(int accountId) throws Exception {
    try {
      Connector connector = Connector.getInstance();
      PreparedStatement statement = connector.prepareStatement(
        "DELETE FROM accounts WHERE id = ?"
      );
      statement.setInt(1, accountId);
      statement.executeUpdate();
      for (Account account : accounts) {
        if (account.getId() == accountId) {
          accounts.remove(account);
          break;
        }
      }
    } catch (Exception e) {
      throw new Exception(e.getMessage(), e.getCause());
    }
  }

  public ArrayList<Account> getAccounts() {
    return accounts;
  }

  public Account getAccountById(int id) {
    for (Account account : accounts) {
      if (account.getId() == id) {
        return account;
      }
    }
    return null;
  }

  public String getName() {
    return name;
  }
}
