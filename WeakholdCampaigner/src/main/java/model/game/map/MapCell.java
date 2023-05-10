package model.game.map;

import model.game.game_entities.Building;
import model.game.game_entities.Unit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MapCell {
    private int defenceFactor;
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

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
    public void addUnit(Unit unit) {
        this.units.add(unit);
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
