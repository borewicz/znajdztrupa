package com.ryhupeja.znajdztrupa.controllers;

import com.ryhupeja.znajdztrupa.Database;
import com.ryhupeja.znajdztrupa.SceneNavigator;
import com.ryhupeja.znajdztrupa.Windows;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by borewicz on 14.01.2016.
 */
public class TrupDetailWindowController implements Argumentable {
    @FXML
    private Label nameLabel, cemeteryLabel, diedLabel, countLabel;
    @FXML
    private Button snitchesButton, modifyButton, removeButton, uploadImageButton, deleteImageButton;
    @FXML
    private ListView<String> photosListView;
    @FXML
    private ImageView imageView;
    @FXML
    private HBox modifyBox;

    private String pesel;
    private boolean liked;

    public void loadData(Object data) {
        int snitchesCount = 0;
        pesel = (String) data;
        ObservableList<String> list = FXCollections.observableArrayList();

        ResultSet result = Database.executeQuery(
                "select t.name, surname, c.name as cemetery_name, pesel, born, died " +
                        "from trupy t " +
                        "inner join places p on p.trupy_pesel=t.pesel " +
                        "inner join cemeteries c on p.cemetery_name=c.name " +
                        "where pesel='" + (String) data + "'");
        ResultSet snitches = Database.executeQuery(
                "select * from snitches where trup_pesel='" + pesel + "'");
        ResultSet photos = Database.executeQuery(
                "select title from photos where trupy_pesel='" + pesel + "'");
        try {
            while (result.next()) {
                nameLabel.setText(result.getString("name") + " " + result.getString("surname"));
                diedLabel.setText(Integer.toString(result.getInt("born")) + " - " +
                        Integer.toString(result.getInt("died")));
                cemeteryLabel.setText(result.getString("cemetery_name"));
            }
            while (snitches.next()) {
                snitchesCount++;
                if (snitches.getString("user_nick").equals(Database.loggedUser))
                    liked = true;
            }
            while (photos.next()) {
                list.add(photos.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (Database.userType != -1) {
            snitchesButton.setVisible(true);
            uploadImageButton.setVisible(true);
            if (Database.userType == 1) {
                modifyBox.setVisible(true);
                deleteImageButton.setVisible(true);
            }
            else {
                modifyBox.setVisible(false);
                deleteImageButton.setVisible(false);
            }
        }
        else {
            snitchesButton.setVisible(false);
            uploadImageButton.setVisible(false);
            modifyBox.setVisible(false);
        }
        photosListView.setItems(list);
        countLabel.setText("Ilość polubień: " + Integer.toString(snitchesCount));
        if (liked) {
            snitchesButton.setText("Nie lubię tego");
        }
        else {
            snitchesButton.setText("Lubię to!");
        }
    }

    @FXML
    protected void snitchButtonClicked(ActionEvent event) {
        if (liked)
            Database.executeUpdate(String.format("delete from snitches where user_nick='%s' and trup_pesel='%s'",
                    Database.loggedUser, pesel));
        else
            Database.executeUpdate(String.format("insert into snitches values ('%s', '%s')",
                    Database.loggedUser, pesel));
        liked = !liked;
        loadData(pesel);
    }

    @FXML
    protected void uploadButtonClicked(ActionEvent event) {
        Windows.showWindow(SceneNavigator.UPLOAD_IMAGE, "Wczytaj zdjęcie", 400, 150, pesel);
    }

    private Image convertToJavaFXImage(byte[] raw, final int width, final int height) {
        WritableImage image = new WritableImage(width, height);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(raw);
            BufferedImage read = ImageIO.read(bis);
            image = SwingFXUtils.toFXImage(read, null);
        } catch (IOException ex) {
//            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }

    @FXML
    protected void photoClicked(MouseEvent event) {
        if (!(photosListView.getSelectionModel().isEmpty())) {
            ResultSet result = Database.executeQuery(
                    String.format("select image from photos where trupy_pesel=\'%s\' and title=\'%s\'",
                            pesel, photosListView.getSelectionModel().getSelectedItem()));
            try {
                while (result.next()) {
                    Blob blob = result.getBlob(1);
                    int blobLength = (int) blob.length();
                    byte[] bytes = blob.getBytes(1, blobLength);
                    blob.free();
//                    byte[] bytes = result.getBytes(1);
                    imageView.setImage(convertToJavaFXImage(bytes, 200, 200));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            System.out.println(photosListView.getSelectionModel().getSelectedItem());
//            SceneNavigator.loadScene(SceneNavigator.CEMETERY_DETAILS, cemeteryListView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    protected void modifyButtonClicked(ActionEvent event) {
        Windows.showWindow(SceneNavigator.NEW_TRUP, "Edytuj trupa", 400, 300,
                new Pair<String, String>(null, pesel));
    }

    @FXML
    protected void deleteButtonClicked(ActionEvent event) {
        if (pesel != null) {
            if ((Windows.showConfirmationMessage("Wszystkie elementy związane z trupem (np. zdjęcia) zostaną usunięte.\n" +
                    "Czy na pewno chcesz usunąć trupa?")) &&
                    (Database.executeUpdate(String.format("delete from trupy where pesel='%s'", pesel)) > 0)) {
//                loadData(null);
                SceneNavigator.goBack();
            }
        }
    }

    @FXML
    protected void deletePhotoClicked(ActionEvent event) {
        String title = photosListView.getSelectionModel().getSelectedItem();
        if (title != null) {
            if ((Windows.showConfirmationMessage("Czy na pewno chcesz usunąć zdjęcie?")) &&
                    (Database.executeUpdate(String.format("delete from photos where trupy_pesel='%s' and title='%s'",
                            pesel, title)) > 0)) {
                loadData(pesel);
                imageView.setImage(null);
            }
        }
    }
}
