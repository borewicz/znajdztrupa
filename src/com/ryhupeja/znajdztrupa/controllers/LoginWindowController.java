package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import com.ryhupeja.znajdztrupa.Windows;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginWindowController {
    @FXML
    private TextField loginTextField, passwordTextField;

    @FXML
    protected void loginButtonClicked(ActionEvent event) {
        try {
            ResultSet result = Database.executeQuery("select * from users where nick='" +
                    loginTextField.getText() + "' and password='" +
                    passwordTextField.getText() + "'");
            if (!result.first()) {
                Windows.showMessage("Nieprawidłowa nazwa użytkownika i/lub hasło. Sprawdź ponownie dane.", AlertType.ERROR);
            } else {
                if (result.getInt("active") == 1) {
                    Database.loggedUser = result.getString("nick");
                    Database.userType = result.getInt("type");
                    SceneNavigator.updateLoggedState("Hi, " + Database.loggedUser);
                    SceneNavigator.goBack();
                }
                else {
                    Windows.showMessage("Konto jest nieaktywne. Poczekaj parę minut lub napisz do administratora.", AlertType.ERROR);
                }
            }
        } catch (SQLException ex) {
            System.out.println("error: " + ex.getMessage());
        }
    }

}
