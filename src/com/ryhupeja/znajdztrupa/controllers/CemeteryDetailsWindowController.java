package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

class Trup {
    private String description;
    private String pesel;

    Trup(String description, String pesel) {
        this.description = description;
        this.pesel = pesel;
    }

    public String toString() {
        return description;
    }

    public String getPesel() {
        return pesel;
    }
}

public class CemeteryDetailsWindowController implements Argumentable {
    @FXML
    private Label nameLabel;
    @FXML
    private ListView<Trup> trupyListView;

    public void loadData(Object data) {
        trupyListView.getItems().clear();
        ObservableList<Trup> list = FXCollections.observableArrayList();
        ResultSet result = Database.executeQuery("select x,y,name,surname,pesel from places p inner join trupy t on p.trup_pesel=t.pesel " +
                "where cemetery_name='" + (String) data + "'");
        try {
            while (result.next()) {
                list.add(new Trup(result.getInt("x") + "," + result.getInt("y") + " - " + result.getString("name") +
                        " " + result.getString("surname"), result.getString("pesel")));
//                list.add(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        trupyListView.setItems(list);
    }

    @FXML
    protected void trupItemClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
//            SceneNavigator.loadScene(SceneNavigator.CEMETERY_DETAILS, cemeteryListView.getSelectionModel().getSelectedItem());
            System.out.println(((Trup)trupyListView.getSelectionModel().getSelectedItem()).getPesel());
        }
    }
}
