package com.ryhupeja.znajdztrupa;

import com.ryhupeja.znajdztrupa.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(SceneNavigator.MAIN));
            VBox root = loader.load();

            MainController mainController = loader.getController();

            SceneNavigator.setMainController(mainController);
            SceneNavigator.loadScene(SceneNavigator.CEMETERY, null);

            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
