package com.ryhupeja.znajdztrupa;

import com.sun.org.apache.xpath.internal.Arg;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Stack;

import com.ryhupeja.znajdztrupa.controllers.*;
import javafx.scene.Node;

public class SceneNavigator {
	private static final String PATH = "scenes/";
    public static final String MAIN    = PATH + "MainWindow.fxml";
    public static final String LOGIN = PATH + "LoginWindow.fxml";
    public static final String CEMETERY = PATH + "CemeteryWindow.fxml";
    public static final String NEW_CEMETERY = PATH + "NewCemeteryWindow.fxml";
//    public static final String VISTA_2 = "vista2.fxml";


    private static MainController mainController;
    private static Stack<String> scenesHistory = new Stack<String>();

    public static void setMainController(MainController mainController) {
    	SceneNavigator.mainController = mainController;
    }
    
    public static void goBack() {
    	if (scenesHistory.size() > 1) {
    		scenesHistory.pop();
    		loadVista(scenesHistory.peek(), null);
    	}
    }

    public static void loadScene(String fxml, Object data) {
    	scenesHistory.push(fxml);
    	loadVista(fxml, data);
    }

    private static boolean isArgumentable(final Class c) {
        return (Argumentable.class.isAssignableFrom(c));
    }
    
    private static void loadVista(String fxml, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxml));
            mainController.setVista((Node) loader.load());
            if (isArgumentable(loader.getController().getClass())) {
                Argumentable controller = loader.getController();
                controller.loadData(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}