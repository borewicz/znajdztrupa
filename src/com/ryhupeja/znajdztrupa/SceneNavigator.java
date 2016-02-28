package com.ryhupeja.znajdztrupa;

import com.ryhupeja.znajdztrupa.controllers.Argumentable;
import com.ryhupeja.znajdztrupa.controllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Stack;

class SavedScene {
    private String fxml;
    private Object data;

    public SavedScene(String fxml, Object data) {
        this.fxml = fxml;
        this.data = data;
    }

    public String getFxml() {
        return fxml;
    }

    public Object getData() {
        return data;
    }
}

public class SceneNavigator {
    private static final String PATH = "scenes/";
    public static final String MAIN = PATH + "MainWindow.fxml";
    public static final String LOGIN = PATH + "LoginWindow.fxml";
    public static final String CEMETERY = PATH + "CemeteryWindow.fxml";
    public static final String NEW_CEMETERY = PATH + "NewCemeteryWindow.fxml";
    public static final String CEMETERY_DETAILS = PATH + "CemeteryDetailsWindow.fxml";
    public static final String NEW_TRUP = PATH + "NewTrupWindow.fxml";
    public static final String TRUP_DETAILS = PATH + "TrupDetailWindow.fxml";
    public static final String UPLOAD_IMAGE = PATH + "UploadImageWindow.fxml";
//    public static final String VISTA_2 = "vista2.fxml";


    private static MainController mainController;
    private static Stack<SavedScene> scenesHistory = new Stack<SavedScene>();

    public static void setMainController(MainController mainController) {
        SceneNavigator.mainController = mainController;
    }

    public static void goBack() {
        if (scenesHistory.size() > 1) {
            scenesHistory.pop();
            loadVista(scenesHistory.peek().getFxml(), scenesHistory.peek().getData());
        }
    }

    public static void loadScene(String fxml, Object data) {
        scenesHistory.push(new SavedScene(fxml, data));
        loadVista(fxml, data);
    }

    public static void updateLoggedState() {
        if (Database.loggedUser != null) {
            mainController.loggedLabel.setText("Cześć, " + Database.loggedUser + "!");
            mainController.loginButton.setText("Wyloguj");
        }
        else {
            mainController.loggedLabel.setText("");
            mainController.loginButton.setText("Zaloguj się");
        }
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