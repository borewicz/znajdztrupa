package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainController {
    @FXML
    private StackPane vistaHolder;
    @FXML
    public Label loggedLabel;

    public void setVista(Node node) {
        vistaHolder.getChildren().setAll(node);
    }

    @FXML
    protected void goBackButtonClicked(ActionEvent event) {
        SceneNavigator.goBack();
    }
}