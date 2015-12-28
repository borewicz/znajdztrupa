package com.ryhupeja.znajdztrupa.controllers;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class CemeteryWindowController implements Initializable {
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
			e.printStackTrace();
		}
		cemeteryListView.setItems(list);
	}
    @FXML protected void loginButtonClicked(ActionEvent event) {
    	SceneNavigator.loadScene(SceneNavigator.LOGIN);
    }
    
}
