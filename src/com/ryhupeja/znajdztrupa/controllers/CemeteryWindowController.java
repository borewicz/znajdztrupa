package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import com.ryhupeja.znajdztrupa.Windows;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CemeteryWindowController implements Argumentable {
    @FXML
    private ListView<String> cemeteryListView;
//    @FXML
//    private Button loginButton;
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
        if (Database.userType == 1) {
            buttonsBox.setVisible(true);
        }
        else {
            buttonsBox.setVisible(false);
        }
    }

    @FXML
    protected void loginButtonClicked(ActionEvent event) {
        SceneNavigator.loadScene(SceneNavigator.LOGIN, null);
    }

    @FXML
    protected void newCemeteryButtonClicked(ActionEvent event) {
        Windows.showWindow(SceneNavigator.NEW_CEMETERY, "Nowy cmentarz", 300, 150, null);
    }

    /*
    @FXML
    protected void modifyCemeteryButtonClicked(ActionEvent event) {
        Windows.showWindow(SceneNavigator.NEW_CEMETERY, "Modify cemetery", 400, 500, cemeteryListView.getSelectionModel().getSelectedItem());
    }
    */

    @FXML
    protected void deleteButtonClicked(ActionEvent event) {
        String name = cemeteryListView.getSelectionModel().getSelectedItem();
        if (name != null) {
            if ((Windows.showConfirmationMessage("Zostaną usunięte wszystkie trupy związane z tym cmentarzem.\n" +
                    "Czy na pewno chcesz go usunąć?")) &&
                    (Database.executeUpdate(String.format("delete from cemeteries where name='%s'", name)) > 0)) {
                loadData(null);
            }
        }
    }

    @FXML
    protected void cemeteryItemClicked(MouseEvent event) {
        if ((event.getClickCount() == 2) && !(cemeteryListView.getSelectionModel().isEmpty())) {
            SceneNavigator.loadScene(SceneNavigator.CEMETERY_DETAILS, cemeteryListView.getSelectionModel().getSelectedItem());
        }
    }

}
