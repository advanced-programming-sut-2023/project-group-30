package model.enums;

public enum Resource {
    WOOD("wood"),
    IRON("iron"),
    STONE("stone"),
    GOLD("gold"),
    WHEAT("wheat"),
    GRAIN("grain"),
    FLOUR("flour"),
    WINE("wine"),
    BREAD("bread"),
    GOLD_COIN("gold coin");
    public final String nameString;

    Resource(String nameString) {
        this.nameString = nameString;
    }

    public String getNameString() {
        return nameString;
    }

}
