package com.ryhupeja.znajdztrupa;

import java.sql.*;

public class Database {
    private static Connection conn;
    public static String loggedUser;
    public static int userType = -1;

    //TODO: singleton

    private static Connection connectToServer() {
        try {
//            return DriverManager.getConnection("jdbc:mysql://localhost/trupy", "root", "Dupa1234");
            Connection conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//admlab2-main.cs.put.poznan.pl:1521/dblab01.cs.put.poznan.pl", "inf117245", "lol");
//            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            return conn;
        } catch (SQLException ex) {
            System.out.println("connectToServer(): " + ex.getMessage());
            return null;
        }
    }

    public static PreparedStatement prepareStatement(String sql) {
        System.out.println(sql);
        try {
            return conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet executeQuery(String query) {
        System.out.println(query);
        if (conn == null)
            conn = connectToServer();
        Statement statement;
        try {
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return statement.executeQuery(query);
        } catch (SQLException e) {
//            Windows.showMessage("Błąd wewnętrzny: " + e.getMessage() + "\nSkontaktuj się z autorem.", AlertType.ERROR);
            System.out.println("executeQuery(): " + e.getMessage());
            return null;
        }
    }

    public static int executeUpdate(String query) {
        System.out.println(query);
        if (conn == null)
            conn = connectToServer();
        Statement statement;
        try {
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return statement.executeUpdate(query);
        } catch (SQLException e) {
//            Windows.showMessage("Błąd wewnętrzny: " + e.getMessage() + "\nSkontaktuj się z autorem.", AlertType.ERROR);
            System.out.println("executeUpdate(): " + e.getMessage());
            return -1;
        }
    }

    public static void setAutoCommit(boolean commit) {
        try {
            conn.setAutoCommit(commit);
        } catch (SQLException e) {
            System.out.println("setAutoCommit(): " + e.getMessage());
        }
    }

    public static void rollback() {
        try {
            conn.rollback();
        } catch (SQLException e) {
            System.out.println("rollback(): " + e.getMessage());
        }
    }

    public static void commit() {
        try {
            conn.commit();
        } catch (SQLException e) {
            System.out.println("commit(): " + e.getMessage());
        }
    }
}
