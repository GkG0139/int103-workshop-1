package com.workshop1.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;

public class Connector {

  private static Connector instance = null;

  private Connection connection;

  private Connector(String url, String username, String password)
    throws SQLException {
    connection = DriverManager.getConnection(url, username, password);
  }

  public static Connector getInstance() throws SQLException {
    if (instance == null) {
      try {
        Dotenv dotenv = Dotenv.load();
        String dbName = dotenv.get("DB_NAME");
        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");

        String url = "jdbc:mysql://localhost:3306/" + dbName;
        instance = new Connector(url, dbUser, dbPassword);
      } catch (Exception e) {
        throw new SQLException("Error: " + e.getMessage());
      }
    }
    return instance;
  }

  public Connection getConnection() {
    return connection;
  }

  public ResultSet executeQuery(String query) throws SQLException {
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(query);
    return resultSet;
  }

  public PreparedStatement prepareStatement(String query) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    return preparedStatement;
  }

  public int executeUpdate(String query) throws SQLException {
    Statement statement = connection.createStatement();
    int rowsAffected = statement.executeUpdate(query);
    return rowsAffected;
  }

  public void close() throws SQLException {
    connection.close();
  }
}
