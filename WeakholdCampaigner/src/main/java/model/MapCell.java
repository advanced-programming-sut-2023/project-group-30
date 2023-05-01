package model;

import model.game_entities.Building;
import model.game_entities.Unit;

import java.util.ArrayList;

public class MapCell {
    private int defenceFactor;
    private ArrayList<Unit> units;
    private Building building;

    public int getDefenceFactor() {
        return defenceFactor;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public Building getBuilding() {
        return building;
    }
}
