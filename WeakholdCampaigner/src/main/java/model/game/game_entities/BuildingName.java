package model.game.game_entities;

import org.jetbrains.annotations.Nullable;

public enum BuildingName {
    KEEP("keep"),
    SMALL_GATEHOUSE("small gatehouse"),
    BIG_GATEHOUSE("big gatehouse"),
    DRAWBRIDGE("drawbridge"),
    LOOKOUT_TOWER("lookout tower"),
    PERIMETER_TOWER("perimeter tower"),
    DEFENCE_TURRET("defence turret"),
    SQUARE_TOWER("square tower"),
    ROUND_TOWER("round tower"),
    SHOP("shop"),
    BARRACKS("Barracks"),
    KILLING_PIT("Killing pit"),
    MOTEL("motel"),

    ;
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
