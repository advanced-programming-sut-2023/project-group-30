package model.game;

import model.enums.Resource;

public class ShopItem {
    private Resource resource;
    private int purchasePrice;
    private int salesPrice;
    private int resourceAmount;

    public ShopItem(Resource resource, int purchasePrice, int salesPrice, int resourceAmount) {
        this.resource = resource;
        this.purchasePrice = purchasePrice;
        this.salesPrice = salesPrice;
        this.resourceAmount = resourceAmount;
    }

    public Resource getResource() {
        return resource;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public int getSalesPrice() {
        return salesPrice;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }
}
