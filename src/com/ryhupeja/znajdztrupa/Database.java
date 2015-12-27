package com.ryhupeja.znajdztrupa;

import java.sql.*;

public class Database {
	private static Connection conn;
    private static Connection connectToServer() {
    	try {
	    	return DriverManager.getConnection("jdbc:mysql://localhost/trupy", "root", "Dupa1234");
	    } catch (SQLException ex) {
	    	System.out.println("error: " + ex.getMessage());
	    	return null;
    	}
    }
    
    public static ResultSet executeQuery(String query)
    {
    	if (conn == null)
    		conn = connectToServer();
		Statement statement;
		try {
			statement = conn.createStatement();
			return statement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
}
