package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import com.ryhupeja.znajdztrupa.Windows;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    @FXML
    private Button newTrupButton, modifyButton, removeButton;

    private String cemeteryName;

    public void loadData(Object data) {
        trupyListView.getItems().clear();
        ObservableList<Trup> list = FXCollections.observableArrayList();
        ResultSet result = Database.executeQuery("select x,y,name,surname,pesel from places p inner join trupy t on p.trup_pesel=t.pesel " +
                "where cemetery_name='" + (String) data + "'");
        try {
            while (result.next()) {
                list.add(new Trup(String.format("%s, %s - %s %s",
                        result.getInt("x"),
                        result.getInt("y"),
                        result.getString("name"),
                        result.getString("surname")),
                        result.getString("pesel")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        trupyListView.setItems(list);
        if (Database.userType == 2) {
            modifyButton.setVisible(true);
            removeButton.setVisible(true);
            newTrupButton.setVisible(true);
        }
        cemeteryName = (String)data;
    }

    @FXML
    protected void newTrupButtonClicked(ActionEvent event) {
        Windows.showWindow(SceneNavigator.NEW_TRUP, "New trup", 400, 500, cemeteryName);
    }

    @FXML
    protected void modifyButtonClicked(ActionEvent event) {
//        Windows.showWindow(SceneNavigator.NEW_TRUP, "Modify trup", 400, 500, trupyListView.getSelectionModel().getSelectedItem());
    }

    @FXML
    protected void deleteButtonClicked(ActionEvent event) {
        String pesel = trupyListView.getSelectionModel().getSelectedItem().getPesel();
        if (pesel != null) {
            if ((Windows.showConfirmationMessage("Czy na pewno chcesz usunąć trupa?")) &&
                    (Database.executeUpdate(String.format("delete from cemeteries where name='%s'", pesel)) > 0)) {
                loadData(null);
            }
        }
    }

    @FXML
    protected void trupItemClicked(MouseEvent event) {
        if ((event.getClickCount() == 2) && !(trupyListView.getSelectionModel().isEmpty())) {
            SceneNavigator.loadScene(SceneNavigator.TRUP_DETAILS, ((Trup)trupyListView.getSelectionModel().getSelectedItem()).getPesel());
        }
    }
}
