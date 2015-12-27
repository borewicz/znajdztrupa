package com.ryhupeja.znajdztrupa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connectToServer() {
    	try {
	    	return DriverManager.getConnection("jdbc:mysql://localhost/trupy", "root", "Dupa1234");
	    } catch (SQLException ex) {
	    	System.out.println("error: " + ex.getMessage());
	    	return null;
    	}
    }
}
