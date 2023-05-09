package model.attributes.building_attributes;

import model.enums.Resource;

public class Harvesting implements BuildingAttribute {
    private final Resource harvestedResource;

    public Harvesting(Resource harvestedResource) {
        this.harvestedResource = harvestedResource;
    }

    public Resource getHarvestedResource() {
        return harvestedResource;
    }
}
