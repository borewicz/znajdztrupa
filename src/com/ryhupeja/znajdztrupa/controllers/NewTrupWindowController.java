package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
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
    private TextField peselTextField, positionXTextField, positionYTextField;

    /*
    jedno miejsce może trzymać wielu trupów
    ale może być tylko na jednym cmentarzu
     */

    /*
    no i danych nie zmieniaj, raz dodany to elo
     */

    /*
    TODO: zdjęcia
     */
    private String cemeteryName, pesel;

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


    public void loadData(Object data) {
        bornDatePicker.setConverter(converter);
        diedDatePicker.setConverter(converter);
        cemeteryName = (String)data;
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

    private boolean checkIfFull()
    {
        String countQuery = String.format("select count(*) from places where cemetery_name='%s'",
                cemeteryName);
        ResultSet countResult = Database.executeQuery(countQuery);
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
                Integer.parseInt(positionXTextField.getText()),
                cemeteryName
                );
        if (Database.executeUpdate(query) > 0) {
            return true;
        }
        return false;
    }

    @FXML
    protected void createButtonClicked(ActionEvent event) {
        int x = Integer.parseInt(positionXTextField.getText());
        int y = Integer.parseInt(positionYTextField.getText());
        if (checkIfFull()) {
            System.out.println("Jest full, elo.");
            return;
        }
        if (addPlace(x, y)) {
            String addTrupQuery = String.format("insert into trupy values (%s, '%s', '%s', '%s', '%s')",
                    peselTextField.getText(),
                    nameTextField.getText(),
                    surnameTextField.getText(),
                    bornDatePicker.getValue() != null ? bornDatePicker.getValue() : "1970-01-01",
                    diedDatePicker.getValue());
            String updatePlaceQuery = String.format("update places set trupy_pesel='%s' where " +
                    "x=%d and y=%d and cemetery_name='%s'", peselTextField.getText(), x, y, cemeteryName);
            if ((Database.executeUpdate(addTrupQuery) > 0) && (Database.executeUpdate(updatePlaceQuery) > 0)) {
                System.out.println("jest fajność");
            }
            else {
                System.out.println("niefajność");
            }
        }
        else {
            System.out.println("Miejsce zajęte, elo.");
            return;
        }
//        if (Database.executeUpdate(query) > 0) {
//            Stage stage = (Stage) closeButton.getScene().getWindow();
//            stage.close();
//            SceneNavigator.loadScene(SceneNavigator.CEMETERY_DETAILS, null);
//        }
    }
}
