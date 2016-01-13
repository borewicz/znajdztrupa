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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CemeteryWindowController implements Argumentable {
    @FXML
    private ListView<String> cemeteryListView;
    @FXML
    private Button loginButton;
    @FXML
    private HBox buttonsBox;

    public void loadData(Object data) {
        cemeteryListView.getItems().clear();
        ObservableList<String> list = FXCollections.observableArrayList();
        ResultSet result = Database.executeQuery("select name from cemeteries");
        try {
            while (result.next()) {
                list.add(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cemeteryListView.setItems(list);
        if (Database.loggedUser == null) {
            buttonsBox.setVisible(false);
        }
        else {
            buttonsBox.setVisible(true);
        }
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
            if (Database.executeUpdate("delete from cemeteries where name=\"" + name + "\"") > 0) {
                loadData(null);
            }
        }
    }

    @FXML
    protected void cemeteryItemClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            SceneNavigator.loadScene(SceneNavigator.CEMETERY_DETAILS, cemeteryListView.getSelectionModel().getSelectedItem());
        }
    }

}
