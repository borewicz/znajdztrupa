package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import com.ryhupeja.znajdztrupa.TrupItem;
import com.ryhupeja.znajdztrupa.Windows;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchWindowController {
    @FXML
    private ListView<TrupItem> resultsListView;
    @FXML
    private TextField nameTextField, surnameTextField;

    @FXML
    protected void searchButtonClicked(ActionEvent event) {
//        if (resultsListView.getItems() != null)
        resultsListView.getItems().clear();
        ObservableList<TrupItem> list = FXCollections.observableArrayList();
        String sql = "select name,surname,pesel from places p inner join trupy t on p.trupy_pesel=t.pesel where ";
        if (!nameTextField.getText().isEmpty())
            sql += String.format("t.name='%s' ", nameTextField.getText());
        if (!surnameTextField.getText().isEmpty()) {
            if (!nameTextField.getText().isEmpty()) sql += "and ";
            sql += String.format("t.surname='%s' ", surnameTextField.getText());
        }
        ResultSet result = Database.executeQuery(sql);
        try {
            while (result.next()) {
                list.add(new TrupItem(String.format("%s %s",
                        result.getString("name"),
                        result.getString("surname")),
                        result.getString("pesel")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resultsListView.setItems(list);
    }
    @FXML
    protected void trupItemClicked(MouseEvent event) {
        if ((event.getClickCount() == 2) && !(resultsListView.getSelectionModel().isEmpty())) {
            SceneNavigator.loadScene(SceneNavigator.TRUP_DETAILS,
                    ((TrupItem)resultsListView.getSelectionModel().getSelectedItem()).getPesel());
        }
    }
}
