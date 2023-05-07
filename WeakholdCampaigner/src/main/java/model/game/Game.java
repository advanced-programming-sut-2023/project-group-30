package model.game;

import model.game.map.Map;
import model.User;

import java.util.LinkedHashMap;

public class Game {
    private int currentTurn;
    private Map map;
    private LinkedHashMap<User, Government> governments;
    private Government currentGovernment;

    public Game(Map map, LinkedHashMap<User, Government> governments, User currentPlayer) {
        this.currentTurn = 0;
        this.map = map;
        this.governments = governments;
        //this.currentPlayer = governments.keySet().iterator().hasNext() ? governments.keySet().iterator().next() : null;
    }
}
