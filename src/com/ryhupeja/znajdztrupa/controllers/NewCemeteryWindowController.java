package com.ryhupeja.znajdztrupa.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.ryhupeja.znajdztrupa.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class NewCemeteryWindowController implements Initializable {
	@FXML private Button closeButton;
	@FXML private TextField cemeteryNameTextField;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		ObservableList<String> list = FXCollections.observableArrayList();
//		ResultSet result = Database.executeQuery("select name from cemetery");
//		try {
//			while (result.next()) {
//				list.add(result.getString(1));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		cemeteryListView.setItems(list);
	}
    @FXML protected void closeButtonClicked(ActionEvent event) {
    	Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    @FXML protected void createButtonClicked(ActionEvent event) {
    	if (Database.executeUpdate("insert into cemetery (name) values (\"" + cemeteryNameTextField.getText() + "\");") > 0) {
    		Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
    	}   		
    }
    
}
