package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import com.ryhupeja.znajdztrupa.Windows;
import com.ryhupeja.znajdztrupa.TrupItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CemeteryDetailsWindowController implements Argumentable {
    @FXML
    private Label nameLabel;
    @FXML
    private ListView<TrupItem> trupyListView;
    @FXML
    private HBox buttonsBox;

    private String cemeteryName;

    public void loadData(Object data) {
        trupyListView.getItems().clear();
        ObservableList<TrupItem> list = FXCollections.observableArrayList();
        ResultSet result = Database.executeQuery("select name,surname,pesel from places p inner join trupy t on p.trupy_pesel=t.pesel " +
                "where cemetery_name='" + (String) data + "'");
        try {
            while (result.next()) {
                list.add(new TrupItem(String.format("%s %s",
//                        result.getInt("x"),
//                        result.getInt("y"),
                        result.getString("name"),
                        result.getString("surname")),
                        result.getString("pesel")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        trupyListView.setItems(list);
        if (Database.userType == 1) {
            buttonsBox.setVisible(true);
        }
        else
            buttonsBox.setVisible(false);
        cemeteryName = (String)data;
    }

    @FXML
    protected void newTrupButtonClicked(ActionEvent event) {
        Windows.showWindow(SceneNavigator.NEW_TRUP, "Dodaj trupa", 400, 300,
                new Pair<String, String>(cemeteryName, null));
    }

    @FXML
    protected void modifyButtonClicked(ActionEvent event) {
        Windows.showWindow(SceneNavigator.NEW_TRUP, "Edytuj trupa", 400, 300,
                new Pair<String, String>(cemeteryName, trupyListView.getSelectionModel().getSelectedItem().getPesel()));
    }

    @FXML
    protected void deleteButtonClicked(ActionEvent event) {
        String pesel = trupyListView.getSelectionModel().getSelectedItem().getPesel();
        if (pesel != null) {
            if ((Windows.showConfirmationMessage("Wszystkie elementy związane z trupem (np. zdjęcia) zostaną usunięte.\n" +
                    "Czy na pewno chcesz usunąć trupa?")) &&
                    (Database.executeUpdate(String.format("delete from trupy where pesel='%s'", pesel)) > 0)) {
                loadData(null);
            }
        }
    }

    @FXML
    protected void trupItemClicked(MouseEvent event) {
        if ((event.getClickCount() == 2) && !(trupyListView.getSelectionModel().isEmpty())) {
            SceneNavigator.loadScene(SceneNavigator.TRUP_DETAILS,
                    ((TrupItem)trupyListView.getSelectionModel().getSelectedItem()).getPesel(), false);
        }
    }
}
