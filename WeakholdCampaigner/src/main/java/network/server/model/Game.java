package network.server.model;

import java.util.ArrayList;

public class Game {
    private int capacity;
    private final ArrayList<String> players;
    public final int ID;

    public Game(int capacity, String admin, int ID) {
        if (capacity > 8 || capacity < 2)
            throw new RuntimeException();

        this.capacity = capacity;
        this.players = new ArrayList<>();
        this.players.add(admin);
        this.ID = ID;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public boolean addPlayer(String player) {
        if (this.players.size() >= capacity)
            return false;
        if (this.players.contains(player))
            return false;

        this.players.add(player);
        return true;
    }

    public boolean hasPlayer(String player) {
        return this.players.contains(player);
    }
}
