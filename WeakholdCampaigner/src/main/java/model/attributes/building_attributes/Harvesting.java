package model.attributes.building_attributes;

import model.enums.Resource;

public class Harvesting implements BuildingAttribute{
    private Resource primarySubstance;
    private Resource finallySubstance;

    public Harvesting(Resource primarySubstance, Resource finallySubstance) {
        this.primarySubstance = primarySubstance;
        this.finallySubstance = finallySubstance;
    }
    public void harvest() {

    }
}
