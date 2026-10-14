package com.hirepath.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DB {
    private DB() {}
    public static Connection getConnection() throws SQLException {
        String url = System.getenv().getOrDefault("HIREPATH_DB_URL",
            "jdbc:mysql://localhost:3306/hirepath?useSSL=false&serverTimezone=UTC");
        String user = System.getenv().getOrDefault("HIREPATH_DB_USER", "root");
        String password = System.getenv().getOrDefault("HIREPATH_DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, password);
    }
}
