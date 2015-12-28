package com.ryhupeja.znajdztrupa;

import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Stack;

import com.ryhupeja.znajdztrupa.controllers.*;

public class SceneNavigator {
    public static final String MAIN    = "MainWindow.fxml";
    public static final String LOGIN = "LoginWindow.fxml";
    public static final String CEMETERY = "CemeteryWindow.fxml";
//    public static final String VISTA_2 = "vista2.fxml";


    private static MainController mainController;
    private static Stack<String> scenesHistory = new Stack<String>();

    public static void setMainController(MainController mainController) {
    	SceneNavigator.mainController = mainController;
    }
    
    public static void goBack() {
    	if (scenesHistory.size() > 1) {
    		scenesHistory.pop();
    		loadVista(scenesHistory.peek());
    	}
    }

    public static void loadScene(String fxml) {
    	scenesHistory.push(fxml);
    	loadVista(fxml);
    }
    
    private static void loadVista(String fxml) {
        try {
            mainController.setVista(
                FXMLLoader.load(
                    SceneNavigator.class.getResource(
                        fxml
                    )
                )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}