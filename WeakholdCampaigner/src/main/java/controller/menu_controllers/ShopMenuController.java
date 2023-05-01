package controller.menu_controllers;

import controller.MainController;
import model.game.Government;
import model.game.Shop;
import model.game.ShopItem;
import view.menus.AppMenu;

public class ShopMenuController {
    public static void showPriceList() {
        Government government = TradeMenuController.getGovernmentByUser(MainController.getCurrentUser());
        int i = 1;
        for (ShopItem shopItem : Shop.getShopItems()) {
            AppMenu.show(i + ": " + shopItem.getResource() + ", purchase_price: " + shopItem.getPurchasePrice() +
                    ", sales_price: " + shopItem.getSalesPrice() + ", you have "
                    + government.getResources().get(shopItem.getResource()) + " number");
            i++;
        }
    }
}
