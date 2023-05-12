package model.game.game_entities;

import model.attributes.Attribute;
import model.attributes.unit_attributes.CloseCombat;
import model.attributes.unit_attributes.RangedAttack;
import model.enums.Resource;
import model.game.map.MapCell;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class Unit extends GameEntity {
    public static final int fieldOfView = 8;
    private int remainingMovement, HP;
    private final int speed, meleeDamage;
    private UnitStance stance;
    public final UnitName unitName;

    public UnitName getUnitName() {
        return unitName;
    }

    private final ArrayList<int[]> destinations;
    private boolean isPatrolling;
    private boolean hasAttacked;
    public final boolean isArab;

    protected Unit(HashMap<Resource, Integer> productionCost, ArrayList<Attribute> attributes, UnitName unitName,
                   int speed, int defence, int attack, int x, int y, boolean isArab) {
        super(productionCost, attributes, x, y);

        if (defence > 5 || defence < 1 || attack > 5 || attack < 1 || speed > 5 || speed < 1) //can be handled better
            throw new RuntimeException("Error: Attempted to instantiate a unit with invalid parameters.");

        this.unitName = unitName;
        this.destinations = new ArrayList<>();
        this.isPatrolling = false;
        this.speed = speed;
        this.remainingMovement = speed;
        this.HP = defence * 10; //fine tune this coefficient
        this.meleeDamage = attack * 4; //fine tune this coefficient
        this.hasAttacked = false;
        this.stance = UnitStance.STAND_GROUND;
        this.isArab = isArab;
    }

    public static Unit getInstance(String name, int x, int y) {
        UnitName unitName = UnitName.getUnitName(name);
        if (unitName == null) return null;

        HashMap<Resource, Integer> productionCost = new HashMap<>();
        ArrayList<Attribute> attributes = new ArrayList<>();
        int speed = 0, defence = 0, attack = 0;
        boolean isArab = false;

        //TODO : use the needed armor and weapons and ...
        switch (unitName) {
            case ARCHER:
                attributes.add(new RangedAttack(6, 8));
                speed = 4;
                defence = 2;
                attack = 2;
                break;
            case CROSSBOWMEN:
                attributes.add(new RangedAttack(5, 10));
                speed = 2;
                defence = 3;
                attack = 2;
                break;
            case SPEARMEN:
                attributes.add(new CloseCombat());
                speed = 3;
                defence = 1;
                attack = 3;
                break;
            case PIKEMEN:
                attributes.add(new CloseCombat());
                speed = 2;
                defence = 4;
                attack = 3;
                break;
            case MACEMEN:
                attributes.add(new CloseCombat());
                speed = 3;
                defence = 3;
                attack = 4;
                break;
            case SWORDSMEN:
                attributes.add(new CloseCombat());
                speed = 1;
                defence = 1;
                attack = 5;
                break;
            case KNIGHT:
                attributes.add(new CloseCombat());
                speed = 5;
                defence = 4;
                attack = 5;
                break;
            case TUNNELER:
                attributes.add(new CloseCombat());
                speed = 4;
                defence = 1;
                attack = 3;
                break;
            case LADDERMEN:
                speed = 4;
                defence = 1;
                break;
            case ENGINEER:
                speed = 3;
                defence = 1;
                break;
            case BLACK_MONK:
                attributes.add(new CloseCombat());
                speed = 1;
                defence = 3;
                attack = 3;
                break;
            case ARCHER_BOW:
                attributes.add(new RangedAttack(6, 8));
                speed = 4;
                defence = 2;
                attack = 2;
                isArab = true;
                break;
            case SLAVES:
                attributes.add(new CloseCombat());
                speed = 4;
                defence = 1;
                attack = 1;
                isArab = true;
                break;
            case SLINGERS:
                attributes.add(new RangedAttack(4, 9));
                speed = 4;
                defence = 1;
                attack = 1;
                isArab = true;
                break;
            case ASSASSINS:
                attributes.add(new CloseCombat());
                speed = 3;
                defence = 3;
                attack = 3;
                isArab = true;
                break;
            case HORSE_ARCHERS:
                attributes.add(new RangedAttack(6, 8));
                speed = 5;
                defence = 3;
                attack = 2;
                isArab = true;
                break;
            case ARABIAN_SWORDSMEN:
                attributes.add(new CloseCombat());
                speed = 5;
                defence = 4;
                attack = 4;
                isArab = true;
                break;
            case FIRE_THROWERS:
                attributes.add(new RangedAttack(5, 9));
                speed = 5;
                defence = 2;
                attack = 4;
                isArab = true;
                break;
        }

        productionCost.put(Resource.GOLD_COIN, 10);

        return new Unit(productionCost, attributes, unitName, speed, defence, attack, x, y, isArab);
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

    public UnitStance getStance() {
        return stance;
    }

    public boolean reduceHP(int decrement) { //returns false if HP hits zero
        this.HP -= decrement;

        if (this.HP <= 0) {
            this.HP = 0;
            return false;
        }
        return true;
    }

    public int getMeleeDamage() {
        return meleeDamage;
    }

    @Nullable
    public int[] getFirstDestination() {
        if (this.destinations.size() == 0) return null;
        return this.destinations.get(0);
    }

    public void removeFirstDestination() {
        if (this.destinations.size() == 0) return;
        this.destinations.remove(0);
    }

    public int howManyDestinations() {
        return this.destinations.size();
    }
}
