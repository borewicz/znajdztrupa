package com.ryhupeja.znajdztrupa;

import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class LoginWindowController {
	@FXML private TextField loginTextField, passwordTextField;
	
    @FXML protected void loginButtonClicked(ActionEvent event) {
    	Connection conn = Database.connectToServer();
    	if (conn != null) {
    		try {
    			Statement stat = conn.createStatement();
    			ResultSet result = stat.executeQuery("select * from users where username='" + 
    					loginTextField.getText() + "' and password='" + 
    					passwordTextField.getText() + "'");
    			if (!result.first()) {	
	    			Message.showMessage("Nieprawidłowa nazwa użytkownika i/lub hasło. Sprawdź ponownie dane.", AlertType.ERROR);
    			}
    			else {
    				if (result.getInt(4) == 1)
    					System.out.println("jest dobrze XD, id konta = " + result.getInt(1));
    				else {
    	    			Message.showMessage("Konto jest nieaktywne. Poczekaj parę minut lub napisz do administratora.", AlertType.ERROR);
    				}
    			} 				
    		}
    		catch (SQLException ex) {
    			System.out.println("error: " + ex.getMessage());
    		}   		
    	}
    }
    
}
