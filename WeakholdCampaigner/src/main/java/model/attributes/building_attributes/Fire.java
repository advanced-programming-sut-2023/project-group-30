package model.attributes.building_attributes;

public class Fire implements BuildingAttribute {
    private Boolean haveFire = false;
    private int turnNumber = 0;

    public Boolean getHaveFire() {
        return haveFire;
    }

    public void turnOn() {
        haveFire = true;
    }

    public void turnOff() {
        haveFire = false;
    }

    public void addTurn() {
        if (haveFire) {
            if (turnNumber == 3)
                haveFire = false;
            turnNumber++;
        }
    }
}
