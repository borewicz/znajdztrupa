package com.ryhupeja.znajdztrupa;

import java.io.IOException;

import com.ryhupeja.znajdztrupa.controllers.Argumentable;
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

	public static void showWindow(String fxml, String title, int width, int height, Object data) {
		Parent root;
		try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxml));
			root = loader.load();
			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(root, width, height));
			stage.show();
            if (data != null) {
                Argumentable controller = loader.getController();
                controller.loadData(data);
            }
//			((Node) (event.getSource())).getScene().getWindow().hide();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
