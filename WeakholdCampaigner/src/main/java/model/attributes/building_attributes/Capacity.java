package model.attributes.building_attributes;

public class Capacity implements BuildingAttribute {
    private final int maxCapacity;
    private final Stored storedItem;

    public Capacity(int maxCapacity, Stored storedItem) {
        this.maxCapacity = maxCapacity;
        this.storedItem = storedItem;
    }

    public Stored getStoredItem() {
        return storedItem;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public enum Stored {
        FOOD, RECOURSE, WEAPON;
    }
}
