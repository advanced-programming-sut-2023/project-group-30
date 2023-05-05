package model.game;

import model.game.map.Map;
import model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Game {
    private int currentTurn;
    private Map map;
    private ArrayList<Government> governments = new ArrayList<>();
    private  Government currentGovernment;

    public Game(Map map, ArrayList<Government> governments, User currentPlayer) {
        this.currentTurn = 0;
        this.map = map;
        //this.governments = //governments;
        //this.currentPlayer = governments.keySet().iterator().hasNext() ? governments.keySet().iterator().next() : null;
    }

    public Government getCurrentGovernment() {
        return currentGovernment;
    }

    public void setCurrentGovernment(Government currentPlayer1) {
        currentGovernment = currentPlayer1;
    }
    public  void addGovernment(Government government) {
        governments.add(government);
    }
    public  ArrayList<Government> getGovernments() {
        return governments;
    }
}
