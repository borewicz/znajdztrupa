package com.ryhupeja.znajdztrupa;

import javafx.scene.control.Alert.AlertType;

import java.sql.*;

public class Database {
    private static Connection conn;
    public static int loggedState = -1;

    private static Connection connectToServer() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/trupy", "root", "Dupa1234");
        } catch (SQLException ex) {
            System.out.println("error: " + ex.getMessage());
            return null;
        }
    }

    public static ResultSet executeQuery(String query) {
        if (conn == null)
            conn = connectToServer();
        Statement statement;
        try {
            statement = conn.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            Windows.showMessage("Błąd wewnętrzny: " + e.getMessage() + "\nSkontaktuj się z autorem.", AlertType.ERROR);
            return null;
        }
    }

    public static int executeUpdate(String query) {
        if (conn == null)
            conn = connectToServer();
        Statement statement;
        try {
            statement = conn.createStatement();
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            Windows.showMessage("Błąd wewnętrzny: " + e.getMessage() + "\nSkontaktuj się z autorem.", AlertType.ERROR);
            return -1;
        }
    }
}
