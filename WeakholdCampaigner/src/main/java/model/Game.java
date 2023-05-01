package model;

import java.util.LinkedHashMap;

public class Game {
    private int currentTurn;
    private Map map;
    private LinkedHashMap<User, Government> governments;
    private User currentPlayer;

    public Game(Map map, LinkedHashMap<User, Government> governments, User currentPlayer) {
        this.currentTurn = 0;
        this.map = map;
        this.governments = governments;
        //this.currentPlayer = governments.keySet().iterator().hasNext() ? governments.keySet().iterator().next() : null;
    }
}
