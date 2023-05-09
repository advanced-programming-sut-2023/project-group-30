package model.game;

import model.enums.Resource;

import java.util.ArrayList;

public class Shop {
    private static ArrayList<ShopItem> shopItems = new ArrayList<>();

    static {
        shopItems.add(new ShopItem(Resource.BREAD, 12, 6, 30));
        shopItems.add(new ShopItem(Resource.FLOUR, 8, 4, 30));
        shopItems.add(new ShopItem(Resource.GOLD, 30, 15, 30));
        shopItems.add(new ShopItem(Resource.IRON, 20, 10, 30));
        shopItems.add(new ShopItem(Resource.GRAIN, 10, 5, 30));
        shopItems.add(new ShopItem(Resource.STONE, 8, 4, 30));
        shopItems.add(new ShopItem(Resource.WHEAT, 13, 6, 30));
        shopItems.add(new ShopItem(Resource.WINE, 20, 7, 30));
        shopItems.add(new ShopItem(Resource.WOOD, 12, 5, 30));
        shopItems.add(new ShopItem(Resource.MEAT, 12, 5, 30));
        shopItems.add(new ShopItem(Resource.CHEESE, 12, 5, 30));
        shopItems.add(new ShopItem(Resource.APPLE, 12, 5, 30));
    }

    public static ShopItem getShopItemByName(Resource resource) {
        for (int i = 0; i < shopItems.size(); i++) {
            if (shopItems.get(i).getResource().equals(resource))
                return shopItems.get(i);
        }
        return null;

    }

    public static ArrayList<ShopItem> getShopItems() {
        return shopItems;
    }
}
