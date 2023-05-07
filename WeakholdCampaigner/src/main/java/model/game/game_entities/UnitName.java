package model.game.game_entities;

import org.jetbrains.annotations.Nullable;

public enum UnitName {
    ARCHER("archer"),
    CROSSBOWMEN("crossbowmen");
    public final String name;

    UnitName(String name) {
        this.name = name;
    }

    @Nullable
    public static UnitName getUnitName(String name) {
        for (UnitName unitName :
                UnitName.values())
            if (name.equals(unitName.name)) return unitName;

        return null;
    }
}
