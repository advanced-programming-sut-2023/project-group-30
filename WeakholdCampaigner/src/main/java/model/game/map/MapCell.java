package model.game.map;

import model.game.game_entities.Building;
import model.game.game_entities.Unit;

import java.util.ArrayList;

public class MapCell {
    public final static ArrayList<Unit> allMapUnits = new ArrayList<>(); //is this ok?
    private int defenceFactor; //TODO
    private ArrayList<Unit> units = null;
    private Building building = null;
    private Texture texture = null;

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public int getDefenceFactor() {
        return defenceFactor;
    }


    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
        allMapUnits.add(unit);
    }

    public void removeUnit(Unit unit) {
        this.units.remove(unit);
        allMapUnits.remove(unit);
    }

    public enum Texture {
        LAND,
        GRAVEL,
        SLATE,
        STONE,
        IRON,
        GRASS,
        GRASSLAND,
        MEADOW,
        SHALLOW_WATER,
        DEEP_WATER,
        OIL,
        BEACH,
        RIVER,
        PLAIN,
    }
}
