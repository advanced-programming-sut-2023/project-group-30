package model.attributes.building_attributes;

public class Tower implements BuildingAttribute {
    public final int fireRange, defenceRange;

    public Tower(int fireRange, int defenceRange) {
        this.fireRange = fireRange;
        this.defenceRange = defenceRange;
    }
}
