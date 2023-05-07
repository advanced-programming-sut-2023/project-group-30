package model.game.map;

import model.game.game_entities.Building;
import model.game.game_entities.Unit;

import java.util.ArrayList;

public class MapCell {
    private int defenceFactor;
    private ArrayList<Unit> units;
    private Building building;
    private Texture texture;

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

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public enum Texture{
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
