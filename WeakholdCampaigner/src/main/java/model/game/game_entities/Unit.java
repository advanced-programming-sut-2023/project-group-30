package model.game.game_entities;

import model.attributes.Attribute;
import model.enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Unit extends GameEntity {
    private int defence, speed, fieldOfView;
    private UnitStance unitStance;

    protected Unit(HashMap<Resource, Integer> productionCost , ArrayList<Attribute> attributes) {
        super(productionCost, attributes);
    }

    public enum UnitStance {
        STAND_GROUND,
        DEFENSIVE_STANCE,
        AGGRESSIVE_STANCE
    }
}
