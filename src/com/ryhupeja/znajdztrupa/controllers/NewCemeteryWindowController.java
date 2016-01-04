package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NewCemeteryWindowController implements Argumentable {
    @FXML
    private Button closeButton;
    @FXML
    private TextField cemeteryNameTextField;
    private int id = -1;

    public void loadData(Object data) {
        if (data != null) {
            ResultSet result = Database.executeQuery("select * from cemetery where name='" + (String) data + "' limit 1");
            try {
                while (result.next()) {
                    id = result.getInt(1);
                    cemeteryNameTextField.setText(result.getString(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void closeButtonClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void createButtonClicked(ActionEvent event) {
        String query;
        if (id != -1)
            query = "update cemetery set name='" + cemeteryNameTextField.getText() + "' where id=" + Integer.toString(id);
        else
            query = "insert into cemetery (name) values ('" + cemeteryNameTextField.getText() + "')";
        if (Database.executeUpdate(query) > 0) {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }
    }

}
