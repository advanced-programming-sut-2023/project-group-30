package model.game_entities;

public abstract class Unit extends GameEntity{
    private int defence, speed, fieldOfView;
    private UnitStance unitStance;

    public enum UnitStance {
        STAND_GROUND,
        DEFENSIVE_STANCE,
        AGGRESSIVE_STANCE
    }
}
