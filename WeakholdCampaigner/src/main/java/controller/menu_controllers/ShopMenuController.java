package controller.menu_controllers;

import controller.messages.MenuMessages;
import model.game.Government;
import model.game.Shop;
import model.game.ShopItem;
import view.menus.AppMenu;


import static model.enums.Resource.GOLD;
import static model.enums.Resource.getResourceByName;

public class ShopMenuController {
    public static void showPriceList() {
        Government government = GameMenuController.getCurrentGame().getCurrentGovernment();
        int i = 1;
        for (ShopItem shopItem : Shop.getShopItems()) {
            AppMenu.show(i + ": " + shopItem.getResource() + ", purchase_price: " + shopItem.getPurchasePrice() +
                    ", sales_price: " + shopItem.getSalesPrice() + ", you have "
                    + government.getResources(shopItem.getResource()) + " number");
            i++;
        }
    }

    public static MenuMessages buyItem(int amount, String item) {
        if (getResourceByName(item) == null || getResourceByName(item) == GOLD)
            return MenuMessages.INVALID_RESOURCE;
        ShopItem shopItem = Shop.getShopItemByName(getResourceByName(item));
        if (shopItem.getResourceAmount() < amount)
            return MenuMessages.INVALID_AMOUNT;
        else if (shopItem.getPurchasePrice() * amount > GameMenuController.getCurrentGame()
                .getCurrentGovernment().getGold())
            return MenuMessages.INVALID_MONEY;//TODO : enough space didn't check
        Government government = GameMenuController.getCurrentGame().getCurrentGovernment();

        if (amount + government.getStoredUnit(government.getResourcesCategory(getResourceByName(item))) >
                government.getMaximumResource(government.getResourcesCategory(getResourceByName(item))))
            return MenuMessages.NOT_ENOUGH_SPACE;
        String answer = AppMenu.getOneLine("are you suer? \n -yes   -no");
        if (answer.equals("no"))
            return MenuMessages.CANCEL;
        else if (!answer.equals("yes"))
            return MenuMessages.INVALID_COMMAND;
        Double previousAmount = GameMenuController.getCurrentGame().getCurrentGovernment()
                .getResources(getResourceByName(item));
        GameMenuController.getCurrentGame().getCurrentGovernment().addResources(getResourceByName(item), amount);
        shopItem.setResourceAmount(shopItem.getResourceAmount() - amount);
        GameMenuController.getCurrentGame().getCurrentGovernment().addGold((-1) * amount * shopItem.getPurchasePrice());
        return MenuMessages.OK;
    }

    public static MenuMessages sellItem(int amount, String item) {
        if (getResourceByName(item) == null || getResourceByName(item) == GOLD)
            return MenuMessages.INVALID_RESOURCE;
        ShopItem shopItem = Shop.getShopItemByName(getResourceByName(item));
        Double previousAmount = GameMenuController.getCurrentGame().getCurrentGovernment()
                .getResources(getResourceByName(item));
        if (amount > previousAmount)
            return MenuMessages.INVALID_AMOUNT;
        String answer = AppMenu.getOneLine("are you suer? \n -yes   -no");
        if (answer.equals("no"))
            return MenuMessages.CANCEL;
        else if (!answer.equals("yes"))
            return MenuMessages.INVALID_COMMAND;
        GameMenuController.getCurrentGame().getCurrentGovernment().addGold(amount * shopItem.getSalesPrice());
        shopItem.setResourceAmount(shopItem.getResourceAmount() + amount);
        GameMenuController.getCurrentGame().getCurrentGovernment().addResources(getResourceByName(item), -amount);
        return MenuMessages.OK;

    }
}
