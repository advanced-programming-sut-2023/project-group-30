package controller.menu_controllers;

import controller.MainController;
import controller.messages.MenuMessages;
import model.Government;
import model.Trade;
import model.User;
import model.enums.Resource;

import java.util.ArrayList;

public class TradeMenuController {
    public static MenuMessages trade (String resource, int resourceAmount, int price, String message) {
        if (getResourceByName(resource) == null)
            return MenuMessages.INVALID_RESOURCE;

        Trade tradeItem = new Trade(MainController.getCurrentUser(), resourceAmount, getResourceByName(resource),
                price, message);

        addTradItem(tradeItem, MainController.getCurrentUser());
        return MenuMessages.OK;
    }
    public static Resource getResourceByName (String name) {
        Resource resources[] = Resource.values();
        for (Resource resource : resources) {
            if (resource.getNameString().equals(name))
                return resource;
        }
        return null;
    }
    public static void addTradItem(Trade tradeItem, User currentUser) {
        ArrayList<Government> governments = GameMenuController.getGovernments();
        for (int i = 0; i < governments.size(); i++) {
            if (!governments.get(i).getOwner().equals(currentUser)){
                governments.get(i).addToTradeList(tradeItem);
            }
        }
    }

}
