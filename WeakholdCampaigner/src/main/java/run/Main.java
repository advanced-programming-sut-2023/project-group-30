package run;

import controller.MainController;
import view.GUI.RegisterMenu;

public class Main { //The application should be started from here for everything to load.
    public static void main(String[] args) {
        MainController mainController = new MainController();
        mainController.initializeApp();
        //mainController.run();
        RegisterMenu.main(args);
    }
}
