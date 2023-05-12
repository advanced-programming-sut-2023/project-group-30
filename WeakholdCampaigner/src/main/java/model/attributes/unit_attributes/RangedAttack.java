package model.attributes.unit_attributes;

public class RangedAttack implements UnitAttribute {
    private final int range, rangedDamage;

    public RangedAttack(int range, int rangedDamage) {
        this.range = range;
        this.rangedDamage = rangedDamage;
    }

    public int getRange() {
        return range;
    }

    public int getRangedDamage() {
        return rangedDamage;
    }
}
