package model.attributes.building_attributes;

import model.game.map.MapCell;

public class NeedsSpecialPlacement implements BuildingAttribute {
    //TODO: apparently you can not place a Farm on Sand
    private final MapCell.Texture neededTexture;

    public NeedsSpecialPlacement(MapCell.Texture neededTexture) {
        this.neededTexture = neededTexture;
    }

    public boolean canBePlacedIn(MapCell.Texture texture) {
        return neededTexture.equals(texture);
    }

    public String getNeededTexture() {
        return neededTexture.toString();
    }
}
