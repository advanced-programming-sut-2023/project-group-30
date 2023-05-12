package model.game.game_entities;

import model.attributes.Attribute;
import model.enums.Resource;
import model.game.map.MapCell;

import java.util.ArrayList;
import java.util.HashMap;

public class Unit extends GameEntity {
    private int remainingMovement, fieldOfView;
    private final int speed, defence;
    private UnitStance stance;
    public final UnitName unitName;

    public UnitName getUnitName() {
        return unitName;
    }

    private final ArrayList<int[]> destinations;
    private boolean isPatrolling;
    private int[] currentLocation;
    private boolean hasAttacked;

    protected Unit(HashMap<Resource, Integer> productionCost, ArrayList<Attribute> attributes, UnitName unitName,
                   int speed, int defence, int x, int y) {
        super(productionCost, attributes);
        this.unitName = unitName;
        this.destinations = new ArrayList<>();
        this.isPatrolling = false;
        this.currentLocation = new int[]{x, y};
        this.speed = speed;
        this.remainingMovement = speed;
        this.defence = defence;
        this.hasAttacked = false;
        this.stance = UnitStance.STAND_GROUND;
    }

    public static Unit getInstance(String name, int x, int y) {
        UnitName unitName = UnitName.getUnitName(name);
        if (unitName == null) return null;

        ArrayList<Attribute> attributes = new ArrayList<>();
        HashMap<Resource, Integer> productionCost = new HashMap<>();
        int speed = 0, defence = 0;

        switch (unitName) {
            //TODO
            case ARCHER:
                productionCost = null;
                break;
        }

        return new Unit(productionCost, attributes, unitName, speed, defence, x, y);
    }

    public boolean isMoving() {
        return destinations.size() > 0;
    }

    public void addDestination(int x, int y) {
        destinations.add(new int[]{x, y});
    }

    public void clearDestinations() {
        destinations.clear();
    }

    public static boolean canGoTo(MapCell.Texture texture) {
        return !texture.equals(MapCell.Texture.DEEP_WATER) &&
                !texture.equals(MapCell.Texture.STONE) &&
                !texture.equals(MapCell.Texture.RIVER);
    }

    public void setCurrentLocation(int x, int y) {
        currentLocation[0] = x;
        currentLocation[1] = y;
    }

    public int getCurrentX() {
        return currentLocation[0];
    }

    public int getCurrentY() {
        return currentLocation[1];
    }

    public int getRemainingMovement() {
        return remainingMovement;
    }

    public void setRemainingMovement(int remaining) {
        this.remainingMovement = remaining;
        if (this.remainingMovement < 0) this.remainingMovement = 0;
    }

    public void resetRemainingMovement() {
        this.remainingMovement = speed;
    }

    public void setPatrolling(boolean patrolling) {
        this.isPatrolling = patrolling;
    }

    public boolean isPatrolling() {
        return isPatrolling;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public boolean hasAttacked() {
        return hasAttacked;
    }

    public enum UnitStance {
        STAND_GROUND,
        DEFENSIVE_STANCE,
        AGGRESSIVE_STANCE
    }

    public void setStance(UnitStance stance) {
        this.stance = stance;
    }
}
