package com.example.myproject1.jdbc;

import com.example.myproject1.exception.DatabaseNotFoundException;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnection {
    private static final String URL = "jdbc:mysql://localhost:3308/GuessANumberGame";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";
    private MySQLConnection() { // to hide constructor
        throw new IllegalStateException("Utility Class");
    }
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            throw new DatabaseNotFoundException(e.getMessage());
        }
    }
}
