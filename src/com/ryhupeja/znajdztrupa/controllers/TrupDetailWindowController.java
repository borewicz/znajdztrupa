package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by borewicz on 14.01.2016.
 */
public class TrupDetailWindowController implements Argumentable {
    @FXML
    private Label nameLabel, surnameLabel, cemeteryLabel, diedLabel;
    @FXML
    private Button snitchesButton, modifyButton, removeButton;

    private String pesel;
    private boolean liked;

    public void loadData(Object data) {
        int snitchesCount = 0;
        pesel = (String) data;
        ResultSet result = Database.executeQuery(
                "select t.name, surname, c.name as cemetery_name, pesel, died " +
                        "from trupy t " +
                        "inner join places p on p.trupy_pesel=t.pesel " +
                        "inner join cemeteries c on p.cemetery_name=c.name " +
                        "where pesel='" + (String) data + "';");
        ResultSet snitches = Database.executeQuery(
                "select * from snitches where trup_pesel='" + pesel + "'");
        try {
            while (result.next()) {
                nameLabel.setText(result.getString("name"));
                surnameLabel.setText(result.getString("surname"));
                diedLabel.setText(Integer.toString(result.getInt("died")));
                cemeteryLabel.setText(result.getString("cemetery_name"));
            }
            while (snitches.next()) {
                snitchesCount++;
                if (snitches.getString("user_nick").equals(Database.loggedUser))
                    liked = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        snitchesButton.setText(Integer.toString(snitchesCount));
        if (Database.userType != -1) {
            snitchesButton.setVisible(true);
            if (Database.userType == 1) {
                modifyButton.setVisible(true);
                removeButton.setVisible(true);
            }
        }
    }

    @FXML
    protected void snitchButtonClicked(ActionEvent event) {
        if (liked)
            Database.executeUpdate(String.format("delete from snitches where user_nick='%s' and trup_pesel='%s'",
                    Database.loggedUser, pesel));
        else
            Database.executeUpdate(String.format("insert into snitches values ('%s', '%s');",
                    Database.loggedUser, pesel));
        liked = !liked;
        loadData(pesel);
    }

}
