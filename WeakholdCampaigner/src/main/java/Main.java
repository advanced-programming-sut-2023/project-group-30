import controller.MainController;
import model.game.map.Map;
import model.game.map.MapCell;

public class Main {
    public static void main(String[] args) {
        MainController mainController = new MainController();
        mainController.initializeApp();
        mainController.run();

    }
}
