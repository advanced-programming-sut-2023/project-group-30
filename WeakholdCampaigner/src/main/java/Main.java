import controller.MainController;
import controller.menu_controllers.GameMenuController;
import model.Database;
import model.game.Game;
import model.game.Government;
import model.game.map.Map;
import model.game.map.MapCell;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        MainController mainController = new MainController();
        mainController.initializeApp();
        mainController.run();

    }
}
