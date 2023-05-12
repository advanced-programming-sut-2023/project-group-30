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
        Government government = new Government(Database.getAllUsers().get(0),0);
        ArrayList<Government> governments = new ArrayList<>();
        governments.add(government);
        Game game = new Game(null,governments);
        game.setCurrentGovernment(government);
        GameMenuController.setCurrentGame(game);
        mainController.run();

    }
}
