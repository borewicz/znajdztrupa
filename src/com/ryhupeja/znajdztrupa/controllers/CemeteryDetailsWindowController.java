package com.ryhupeja.znajdztrupa.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by borewicz on 05.01.2016.
 */
public class CemeteryDetailsWindowController implements Argumentable {
    @FXML
    private Label nameLabel;
    public void loadData(Object data) {
        nameLabel.setText((String)data);
    }
}
