package com.middleearth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {
    private static Connection connection = null;

    private DatabaseConfiguration() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String url = "jdbc:mysql://localhost:3306/lotr_db";
                String user = "root";
                String password = "your_password";
                connection = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}