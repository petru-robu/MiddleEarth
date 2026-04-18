package com.middleearth.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfiguration {
    private static Connection connection = null;

    private static String url = "jdbc:mysql://localhost:3306/lotr_db";
    private static String user = "lotr_user";
    private static String password = "pass";

    private DatabaseConfiguration() {}

    public static void configure(String url, String user, String password) {
        DatabaseConfiguration.url = url;
        DatabaseConfiguration.user = user;
        DatabaseConfiguration.password = password;
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    public static void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
    }

    public static void execute(String sql) throws SQLException {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(sql);
        }
    }
}