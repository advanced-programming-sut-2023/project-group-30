package model.game_entities;

public enum EntityName {
    ARCHER("Archer"),
    CROSSBOWMEN("Crossbowmen");
    public final String name;

    EntityName(String name) {
        this.name = name;
    }
}
