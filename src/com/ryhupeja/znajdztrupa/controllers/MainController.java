package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private StackPane vistaHolder;
    @FXML
    public Label loggedLabel;
    @FXML
    public Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loggedLabel.setText("Not logged.");
        loginButton.setText("Login");
    }

    public void setVista(Node node) {
        vistaHolder.getChildren().setAll(node);
    }

    @FXML
    protected void goBackButtonClicked(ActionEvent event) {
        SceneNavigator.goBack();
    }

    @FXML
    protected void loginButtonClicked(ActionEvent event) {
        if (Database.loggedUser == null)
            SceneNavigator.loadScene(SceneNavigator.LOGIN, null);
        else {
            Database.loggedUser = null;
            SceneNavigator.loadScene(SceneNavigator.CEMETERY, null);
            SceneNavigator.updateLoggedState();
        }
    }
}