package com.ryhupeja.znajdztrupa;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Windows {
	public static void showMessage(String text, AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle("Słabo");
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
	}

	public static void showWindow(String fxml, String title, int width, int height) {
		Parent root;
		try {
			root = FXMLLoader.load(SceneNavigator.class.getResource(fxml));
			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(root, width, height));
			stage.show();
//			((Node) (event.getSource())).getScene().getWindow().hide();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
