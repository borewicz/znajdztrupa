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
    private Button likesButton;

    private String pesel;
    private boolean liked;

    public void loadData(Object data) {
        int likesCount = 0;
        pesel = (String) data;
//        System.out.println(pesel);
        ResultSet result = Database.executeQuery(
                "select t.name, surname, c.name as cemetery_name, pesel, died " +
                        "from trupy t " +
                        "inner join places p on p.trup_pesel=t.pesel " +
                        "inner join cemeteries c on p.cemetery_name=c.name " +
                        "where pesel='" + (String) data + "';");
        ResultSet likes = Database.executeQuery(
                "select * from likes where trupy_pesel='" + pesel + "'");
        try {
            while (result.next()) {
                nameLabel.setText(result.getString("name"));
                surnameLabel.setText(result.getString("surname"));
                diedLabel.setText(Integer.toString(result.getInt("died")));
                cemeteryLabel.setText(result.getString("cemetery_name"));
            }
            while (likes.next()) {
                likesCount++;
                if (likes.getString("users_nick").equals(Database.loggedUser))
                    liked = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        likesButton.setText(Integer.toString(likesCount));
        if (Database.loggedUser == null)
            likesButton.setVisible(false);
    }

    @FXML
    protected void likeButtonClicked(ActionEvent event) {
        //insert into likes values ("jan", "2222");
        //delete from likes where users_nick='jan' and trupy_pesel='2222';
        if (liked)
            Database.executeUpdate(String.format("delete from likes where users_nick='%s' and trupy_pesel='%s'",
                    Database.loggedUser, pesel));
        else
            Database.executeUpdate(String.format("insert into likes values ('%s', '%s');",
                    Database.loggedUser, pesel));
        liked = !liked;
        loadData(pesel);
    }

}
