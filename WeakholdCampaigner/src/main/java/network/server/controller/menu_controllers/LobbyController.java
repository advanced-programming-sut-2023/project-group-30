package network.server.controller.menu_controllers;

import network.server.Database;
import network.server.model.Game;

public class LobbyController {
    public static String createGame(int capacity, String admin) {
        if (capacity > 8 || capacity < 2)
            return "Capacity must be between 2 and 8";

        Game game = new Game(capacity, admin, Database.getGames().size() + 1);
        Database.addGame(game);

        return "Success";
    }
}
