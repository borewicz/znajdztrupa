package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import com.ryhupeja.znajdztrupa.Windows;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NewTrupWindowController implements Argumentable {
    @FXML
    private Button closeButton;
    @FXML
    private TextField nameTextField, surnameTextField, bornTextField, diedTextField,
            peselTextField, positionXTextField, positionYTextField;

    private String cemeteryName, pesel;

    public void loadData(Object data) {
        Pair<String, String> args = (Pair<String, String>)data;
        cemeteryName = args.getKey();
        if (args.getValue() != null) {
            pesel = args.getValue();
            ResultSet result = Database.executeQuery("select * from trupy where pesel='" + pesel + "'");
            if (result != null)
                try {
                    while (result.next()) {
                        peselTextField.setText(result.getString(1));
                        nameTextField.setText(result.getString(2));
                        surnameTextField.setText(result.getString(3));
                        if (result.getInt(4) != -1)
                            bornTextField.setText(Integer.toString(result.getInt(4)));
                        diedTextField.setText(Integer.toString(result.getInt(5)));
                        positionXTextField.setEditable(false);
                        positionYTextField.setEditable(false);
                        peselTextField.setEditable(false);
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

    private boolean checkIfFull()
    {
        String countQuery = String.format("select count(*) from places where cemetery_name='%s'",
                cemeteryName);
        ResultSet countResult = Database.executeQuery(countQuery);
        if (countResult != null)
            try {
                while (countResult.next()) {
                    return countResult.getInt(1) > 9000; // tak jak pełny
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return false;
    }

//    private boolean checkIfExist(

    private boolean addPlace(int x, int y) {
        //jak nie ma to utworzy

        String query = String.format("insert into places values (%d, %d, '%s', NULL)",
                Integer.parseInt(positionXTextField.getText()),
                Integer.parseInt(positionYTextField.getText()),
                cemeteryName
                );
        if (Database.executeUpdate(query) > 0) {
            return true;
        }
        return false;
    }

    @FXML
    protected void createButtonClicked(ActionEvent event) {
        if (pesel != null) {
            String updateQuery = String.format("update trupy set name='%s'," +
                    "surname='%s', born=%s, died=%s where pesel='%s'",
                    nameTextField.getText(),
                    surnameTextField.getText(),
                    bornTextField.getText().isEmpty() ? "-1" : bornTextField.getText(),
                    diedTextField.getText(),
                    pesel);
            if (Database.executeUpdate(updateQuery) > 0) {
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();
                SceneNavigator.loadScene(SceneNavigator.CEMETERY_DETAILS, cemeteryName);
            }
        }
        else {
            int x = Integer.parseInt(positionXTextField.getText());
            int y = Integer.parseInt(positionYTextField.getText());
            if (checkIfFull()) {
                System.out.println("Jest full, elo.");
                return;
            }
            if (addPlace(x, y)) {
                String addTrupQuery = String.format("insert into trupy values (%s, '%s', '%s', %s, %s)",
                        peselTextField.getText(),
                        nameTextField.getText(),
                        surnameTextField.getText(),
                        bornTextField.getText().isEmpty() ? "-1" : bornTextField.getText(),
                        diedTextField.getText());
                String updatePlaceQuery = String.format("update places set trupy_pesel='%s' where " +
                        "x=%d and y=%d and cemetery_name='%s'", peselTextField.getText(), x, y, cemeteryName);
                if ((Database.executeUpdate(addTrupQuery) > 0) && (Database.executeUpdate(updatePlaceQuery) > 0)) {
                    Stage stage = (Stage) closeButton.getScene().getWindow();
                    stage.close();
                    SceneNavigator.loadScene(SceneNavigator.CEMETERY_DETAILS, cemeteryName);
                } else {
                    Windows.showMessage("Coś się zepsuło i nie było mnie słychać.", Alert.AlertType.ERROR);
                }
            } else {
                Windows.showMessage("Miejsce jest już zajęte.", Alert.AlertType.INFORMATION);
            }
        }
    }
}
