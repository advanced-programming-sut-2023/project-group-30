package model.game;

import model.game.game_entities.Building;
import model.game.game_entities.Unit;
import model.game.map.Map;
import model.User;
import model.game.map.MapCell;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public int getMapX() {
        return map.getWidth();
    }

    public int getMapY() {
        return map.getWidth();
    }

    @Nullable
    public Building getBuilding(int x, int y) {
        return map.getCell(x, y).getBuilding();
    }

    public void dropBuilding(Building building, int x, int y) {
        building.setGovernmentColor(currentGovernment.getColor());
        map.getCell(x, y).setBuilding(building);
    }

    @NotNull
    public ArrayList<Unit> getUnits(int x, int y) {
        return map.getCell(x, y).getUnits();
    }

    @NotNull
    public MapCell.Texture getTexture(int x, int y) {
        return map.getCell(x, y).getTexture();
    }

    public Government getCurrentGovernment() {
        return currentGovernment;
    }
}
