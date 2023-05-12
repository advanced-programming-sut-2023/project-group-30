package model.game.game_entities;

import org.jetbrains.annotations.Nullable;

public enum UnitName {
    ARCHER("Archer"),
    CROSSBOWMEN("Crossbowmen"),
    SPEARMEN("Spearmen"),
    PIKEMEN("Pikemen"),
    MACEMEN("Macemen"),
    SWORDSMEN("Swordsmen"),
    KNIGHT("Knight"),
    TUNNELER("Tunneler"),
    LADDERMEN("Laddermen"),
    ENGINEER("Engineer"),
    BLACK_MONK("Black Monk"),
    ARCHER_BOW("Archer Bow"),
    SLAVES("Slaves"),
    SLINGERS("Slingers"),
    ASSASSINS("Assassins"),
    HORSE_ARCHERS("Horse Archers"),
    ARABIAN_SWORDSMEN("Arabian Swordsmen"),
    FIRE_THROWERS("Fire Throwers"),
    ;
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
