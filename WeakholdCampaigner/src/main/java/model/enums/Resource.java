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
    GOLD_COIN("gold coin"),
    MEAT("meat"),
    CHEESE("cheese"),
    APPLE("apple");
    public final String nameString;

    Resource(String nameString) {
        this.nameString = nameString;
    }

    public String getNameString() {
        return nameString;
    }
    public static Resource getResourceByName(String name) {
        Resource resources[] = Resource.values();
        for (Resource resource : resources) {
            if (resource.getNameString().equals(name))
                return resource;
        }
        return null;
    }

}
