package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import com.ryhupeja.znajdztrupa.Windows;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginWindowController {
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordField;

    private String getMD5Hash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes(),0,text.length());
            return new BigInteger(1,md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    @FXML
    protected void loginButtonClicked(ActionEvent event) {
        try {
            ResultSet result = Database.executeQuery(String.format("select * from users where nick='%s' and password='%s'",
                    loginTextField.getText(), getMD5Hash(passwordField.getText())));
            if (!result.first()) {
                Windows.showMessage("Nieprawidłowa nazwa użytkownika i/lub hasło. Sprawdź ponownie dane.", AlertType.ERROR);
            } else {
                if (result.getInt("active") == 1) {
                    Database.loggedUser = result.getString("nick");
                    Database.userType = result.getInt("type");
                    SceneNavigator.updateLoggedState();
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
