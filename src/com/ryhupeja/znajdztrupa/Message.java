package com.ryhupeja.znajdztrupa;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Message {
	public static void showMessage(String text, AlertType alertType)
	{
		Alert alert = new Alert(alertType);
		alert.setTitle("Słabo");
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
	}
}
