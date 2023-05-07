package model.game.game_entities;

import model.attributes.Attribute;
import model.enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class Unit extends GameEntity {
    private int defence, speed, fieldOfView;
    private UnitStance unitStance;
    public final UnitName unitName;

    protected Unit(HashMap<Resource, Integer> productionCost , ArrayList<Attribute> attributes, UnitName unitName) {
        super(productionCost, attributes);
        this.unitName = unitName;
    }

    public static Unit getInstance(String name) {
        UnitName unitName = UnitName.getUnitName(name);
        if (unitName == null) return null;

        ArrayList<Attribute> attributes = new ArrayList<>();
        HashMap<Resource, Integer> productionCost = new HashMap<>();

        switch (unitName) {
            //TODO
            case ARCHER:
                productionCost = null;
                break;
        }

        return new Unit(productionCost, attributes, unitName);
    }

    public enum UnitStance {
        STAND_GROUND,
        DEFENSIVE_STANCE,
        AGGRESSIVE_STANCE
    }
}
