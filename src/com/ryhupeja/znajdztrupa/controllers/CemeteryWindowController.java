package com.ryhupeja.znajdztrupa.controllers;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import com.ryhupeja.znajdztrupa.Windows;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class CemeteryWindowController implements Initializable {
	@FXML private ListView<String> cemeteryListView;
	@FXML private Button loginButton;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
        update();
	}

    public void update()
    {
        cemeteryListView.getItems().clear();
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
    @FXML protected void newCemeteryButtonClicked(ActionEvent event) {
    	Windows.showWindow(SceneNavigator.NEW_CEMETERY, "New cemetery", 400, 500);
    }
	@FXML protected void deleteButtonClicked(ActionEvent event) {
		String name = cemeteryListView.getSelectionModel().getSelectedItem();
        if (name != null) {
            if (Database.executeUpdate("delete from cemetery where name=\"" + name + "\"") > 0) {
                update();
            }
        }
	}
    
}
