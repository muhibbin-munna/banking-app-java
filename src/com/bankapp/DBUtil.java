package com.bankapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:ORCL";
    private static final String DB_USERNAME = "munna";
    private static final String DB_PASSWORD = "munna1234";

    public static Connection getConnection() throws SQLException {
        try {
            // Load and register the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new SQLException("Connection to DB failed.", e);
        }
    }
}
