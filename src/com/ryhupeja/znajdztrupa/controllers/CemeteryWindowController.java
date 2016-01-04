package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import com.ryhupeja.znajdztrupa.Windows;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CemeteryWindowController implements Argumentable {
    @FXML
    private ListView<String> cemeteryListView;
    @FXML
    private Button loginButton;

    public void loadData(Object data) {
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

    @FXML
    protected void loginButtonClicked(ActionEvent event) {
        SceneNavigator.loadScene(SceneNavigator.LOGIN, null);
    }

    @FXML
    protected void newCemeteryButtonClicked(ActionEvent event) {
        Windows.showWindow(SceneNavigator.NEW_CEMETERY, "New cemetery", 400, 500, null);
    }

    @FXML
    protected void modifyCemeteryButtonClicked(ActionEvent event) {
        Windows.showWindow(SceneNavigator.NEW_CEMETERY, "Modify cemetery", 400, 500, cemeteryListView.getSelectionModel().getSelectedItem());
    }

    @FXML
    protected void deleteButtonClicked(ActionEvent event) {
        String name = cemeteryListView.getSelectionModel().getSelectedItem();
        if (name != null) {
            if (Database.executeUpdate("delete from cemetery where name=\"" + name + "\"") > 0) {
                loadData(null);
            }
        }
    }

}
