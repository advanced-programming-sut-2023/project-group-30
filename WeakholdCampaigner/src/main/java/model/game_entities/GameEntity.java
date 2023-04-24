package model.game_entities;

import model.Enum.Resource;
import model.attributes.Attribute;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameEntity {
    private HashMap<Resource, Integer> productionCost;
    private EntityName name;
    private ArrayList<Attribute> attributes;

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }
}
