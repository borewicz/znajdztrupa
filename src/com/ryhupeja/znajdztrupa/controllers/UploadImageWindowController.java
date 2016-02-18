package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.Windows;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by borewicz on 18.02.2016.
 */
public class UploadImageWindowController implements Argumentable {
    @FXML
    private Button closeButton;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private Label fileNameLabel;
    private String pesel, filePath;
    final FileChooser fileChooser = new FileChooser();

    public void loadData(Object data) {
        pesel = (String) data;
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));
    }

    @FXML
    protected void closeButtonClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void browseButtonClicked() {
        File file = fileChooser.showOpenDialog(closeButton.getScene().getWindow());
        if (file != null) {
            filePath = file.getAbsolutePath();
            fileNameLabel.setText(file.getName());
        }
    }

    @FXML
    protected void uploadButtonClicked(ActionEvent event) {
        Windows.showMessage(filePath, Alert.AlertType.INFORMATION);

        String sql = "insert into photos values (?, ?, ?)";
        File file = new File(filePath);
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        PreparedStatement statement = Database.prepareStatement(sql);
        if (statement != null) {
            try {
                statement.setBinaryStream(1, input);
                statement.setString(2, descriptionTextField.getText());
                statement.setString(3, pesel);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
