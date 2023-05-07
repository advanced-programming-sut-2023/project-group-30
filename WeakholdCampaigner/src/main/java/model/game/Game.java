package model.game;

import model.game.map.Map;
import model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Game {
    private int currentTurn;
    private Map map;
    private ArrayList<Government> governments;
    private Government currentGovernment;

    public Game(Map map, ArrayList<Government> governments) {
        this.currentTurn = 0;
        this.map = map;
        this.governments = governments;
        this.currentGovernment = governments.get(0);
    }
}
