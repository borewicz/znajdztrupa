package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewCemeteryWindowController implements Argumentable {
    @FXML
    private TextField cemeteryNameTextField;
    private String oldName;

    public void loadData(Object data) {
        /*
        if (data != null) {
            ResultSet result = Database.executeQuery("select * from cemeteries where name='" + (String) data + "'");
            try {
                while (result.next()) {
                    oldName = result.getString(1);
                    cemeteryNameTextField.setText(oldName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        */
    }

    @FXML
    protected void closeButtonClicked(ActionEvent event) {
        Stage stage = (Stage) cemeteryNameTextField.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void createButtonClicked(ActionEvent event) {
//        if (oldName != null)
//            query = "update cemeteries set name='" + cemeteryNameTextField.getText() + "' where name='" + oldName + "'";
//        else
        String query = "insert into cemeteries (name) values ('" + cemeteryNameTextField.getText() + "')";
        if (Database.executeUpdate(query) > 0) {
            Stage stage = (Stage) cemeteryNameTextField.getScene().getWindow();
            stage.close();
            SceneNavigator.loadScene(SceneNavigator.CEMETERY, null, true);
        }
    }

}
