package model.game.game_entities;

import org.jetbrains.annotations.Nullable;

public enum BuildingName {
    A("");

    private final String name;

    BuildingName(String name) {
        this.name = name;
    }

    @Nullable
    public static BuildingName getBuildingName(String name) {
        for (BuildingName buildingName :
                BuildingName.values())
            if (name.equals(buildingName.name)) return buildingName;

        return null;
    }
}
