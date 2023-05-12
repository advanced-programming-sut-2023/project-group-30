package model.attributes.building_attributes;

import model.enums.Resource;

public class Process implements BuildingAttribute {
    private Resource primarySubstance;
    private Resource finallySubstance;

    public Process(Resource primarySubstance, Resource finallySubstance) {
        this.primarySubstance = primarySubstance;
        this.finallySubstance = finallySubstance;
    }

    public Resource getPrimarySubstance() {
        return primarySubstance;
    }

    public Resource getFinallySubstance() {
        return finallySubstance;
    }
}
