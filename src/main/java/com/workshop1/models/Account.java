package com.workshop1.models;

import com.workshop1.database.Connector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Account {

  private int id;

  private String name;
  private int balance;

  public Account(String name, int balance) {
    this.name = name;
    this.balance = balance;

    try {
      Connector connector = Connector.getInstance();
      PreparedStatement statement = connector.prepareStatement(
        "SELECT MAX(id) AS max_id FROM accounts"
      );
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        id = resultSet.getInt("max_id") + 1;
      } else {
        id = 0;
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  public Account(int id, String name, int balance) {
    this.id = id;
    this.name = name;
    this.balance = balance;
  }

  public int getId() {
    return id;
  }

  public int deposit(int amount) throws Exception {
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be greater than 0");
    }

    try {
      Connector connector = Connector.getInstance();
      PreparedStatement statement = connector.prepareStatement(
        "UPDATE accounts SET balance = ? WHERE id = ?"
      );
      statement.setInt(1, balance + amount);
      statement.setInt(2, id);
      statement.executeUpdate();

      return balance += amount;
    } catch (Exception e) {
      throw new Exception(e.getMessage(), e.getCause());
    }
  }

  public int withdraw(int amount) throws Exception {
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be greater than 0");
    }

    if (amount > balance) {
      throw new IllegalArgumentException("Not enough balance");
    }

    try {
      Connector connector = Connector.getInstance();
      PreparedStatement statement = connector.prepareStatement(
        "UPDATE accounts SET balance = ? WHERE id = ?"
      );
      statement.setInt(1, balance - amount);
      statement.setInt(2, id);
      statement.executeUpdate();

      return balance -= amount;
    } catch (Exception e) {
      throw new Exception(e.getMessage(), e.getCause());
    }
  }

  public int getBalance() {
    return balance;
  }

  public String getName() {
    return name;
  }
}
