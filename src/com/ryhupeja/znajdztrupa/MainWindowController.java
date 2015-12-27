package com.ryhupeja.znajdztrupa;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainWindowController implements Initializable {
	@FXML private ListView<String> cemeteryListView;
	@FXML private Button loginButton;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> list = FXCollections.observableArrayList();
		ResultSet result = Database.executeQuery("select name from cemetery");
		try {
			while (result.next()) {
				list.add(result.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cemeteryListView.setItems(list);
	}
    @FXML protected void loginButtonClicked(ActionEvent event) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("LoginWindow.fxml"));
			Scene scene = new Scene(root,400,400);
			Stage primaryStage = new Stage();
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
//            ((Node)(event.getSource())).getScene().getWindow().hide();

		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
}
