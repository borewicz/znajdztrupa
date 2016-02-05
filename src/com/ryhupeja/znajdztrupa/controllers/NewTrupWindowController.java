package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.NumberTextField;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NewTrupWindowController implements Argumentable {
    @FXML
    private Button closeButton, createButton;
    @FXML
    private TextField nameTextField, surnameTextField;
    @FXML
    private DatePicker bornDatePicker, diedDatePicker;
    @FXML
    private TextField peselTextField;

    StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }

        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    };

    private String pesel;

    public void loadData(Object data) {
        bornDatePicker.setConverter(converter);
        diedDatePicker.setConverter(converter);
        /*
        if (data != null) {
            ResultSet result = Database.executeQuery("select * from cemeteries where name='" + (String) data + "' limit 1");
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

    /*
    @FXML
    private TextField cemeteryNameTextField;
    private String oldName;


*/
    @FXML
    protected void closeButtonClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void createButtonClicked(ActionEvent event) {
        String query;
//        if (pesel != null)
//            query = "update cemeteries set name='" + cemeteryNameTextField.getText() + "' where name='" + oldName + "'";
//        else
            query = String.format("insert into trupy values (%s, \"%s\", \"%s\", \"%s\", \"%s\")",
                    peselTextField.getText(),
                    nameTextField.getText(),
                    surnameTextField.getText(),
                    bornDatePicker.getValue() != null ? bornDatePicker.getValue() : "1970-01-01",
                    diedDatePicker.getValue());
        System.out.println(query);
        if (Database.executeUpdate(query) > 0) {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
            SceneNavigator.loadScene(SceneNavigator.CEMETERY_DETAILS, null);
        }
    }
}
