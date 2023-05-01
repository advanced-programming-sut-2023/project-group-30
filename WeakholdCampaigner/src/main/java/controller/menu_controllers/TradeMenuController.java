package controller.menu_controllers;

import controller.MainController;
import controller.messages.MenuMessages;
import model.Government;
import model.Trade;
import model.User;
import model.enums.Resource;
import view.menus.AppMenu;

import java.util.ArrayList;

public class TradeMenuController {
    public static MenuMessages trade (String resource, int resourceAmount, int price, String message) {
        if (getResourceByName(resource) == null)
            return MenuMessages.INVALID_RESOURCE;
        if (getGovernmentByUser(MainController.getCurrentUser()).getResources().get(Resource.GOLD_COIN) < price)
            return MenuMessages.INVALID_MONEY;

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
            else governments.get(i).addToTradeHistory(tradeItem);
        }
    }
    public static Government getGovernmentByUser(User currentUser) {
        ArrayList<Government> governments = GameMenuController.getGovernments();
        for (int i = 0; i < governments.size(); i++) {
            if (governments.get(i).getOwner().equals(currentUser)){
                return governments.get(i);
            }
        }
        return null;

    }

    public static void showTradeList() {
        ArrayList<Trade> tradeList = getGovernmentByUser(MainController.getCurrentUser()).getTradeList();
        for (Trade trade : tradeList)
            AppMenu.show(trade.getId() + ": resource_type:" + trade.getResourceType() + ", resource_amount "
                    + trade.getResourceAmount() + ", price: " + trade.getPrice() + ", requested_user: "
                    + trade.getApplicant().getUsername() + ", message: " + trade.getMessage());
    }
    public static Trade getTradeById(int id) {
        ArrayList<Trade> tradeList = getGovernmentByUser(MainController.getCurrentUser()).getTradeList();
        for (Trade trade : tradeList) {
            if (trade.getId() == id)
                return trade;
        }
        return null;
    }

    public static MenuMessages tradeAccept(int id, String message) {
        Trade trade = getTradeById(id);
        Government government = getGovernmentByUser(MainController.getCurrentUser());
        Government applicantGovernment = getGovernmentByUser(trade.getApplicant());

        if (trade.getResourceAmount() > government.getResources().get(trade.getResourceType()))
            return MenuMessages.INVALID_AMOUNT;

        government.getResources().put(trade.getResourceType(), government.getResources().get(trade.getResourceType()) -
                trade.getResourceAmount());

        removeFromTradList(trade.getId());

        government.addToTradeHistory(new Trade(trade.getApplicant(), trade.getResourceAmount(),
                trade.getResourceType(), trade.getPrice(), message));

        applicantGovernment.getResources().put(Resource.GOLD_COIN,
                applicantGovernment.getResources().get(Resource.GOLD_COIN) - trade.getPrice());

        government.getResources().put(Resource.GOLD_COIN,
                government.getResources().get(Resource.GOLD_COIN) + trade.getPrice());


        return MenuMessages.OK;
    }
    public static void removeFromTradList(int id) {
        ArrayList<Government> governments = GameMenuController.getGovernments();
        for (int i = 0; i < governments.size(); i++) {
            ArrayList<Trade> tradeList = governments.get(i).getTradeList();
            ArrayList<Trade> tradeNotification = governments.get(i).getTradeNotification();
            for (int j = 0; j < tradeList.size(); j++) {
                if (tradeList.get(j).getId() == id) {
                    tradeList.remove(j);
                    j--;
                }
            }
            for (int j = 0; j < tradeList.size(); j++) {
                if (tradeNotification.get(j).getId() == id) {
                    tradeNotification.remove(j);
                    j--;
                }
            }
        }
    }

    public static void showTradeHistory() {
        User user = MainController.getCurrentUser();
        Government government = getGovernmentByUser(user);
        AppMenu.show("requests:");
        for (Trade trade : government.getTradeHistory()) {
            if (trade.getApplicant().equals(user))
                AppMenu.show(trade.getId() + ": resource_type:" + trade.getResourceType() + ", resource_amount "
                        + trade.getResourceAmount() + ", price: " + trade.getPrice()
                        + ", message: " + trade.getMessage());
        }
        AppMenu.show("accepted:");
        for (Trade trade : government.getTradeHistory()) {
            if (!trade.getApplicant().equals(user))
                AppMenu.show(trade.getId() + ": resource_type:" + trade.getResourceType() + ", resource_amount "
                        + trade.getResourceAmount() + ", price: " + trade.getPrice() + ", requested_user: "
                        + trade.getApplicant().getUsername()
                        + ", message: " + trade.getMessage());
        }


    }

    public static void enterTradeMenu() {
        MainController.setCurrentMenu(AppMenu.MenuName.TRAD_MENU);
        AppMenu.show("entered trade menu");
        Government government = getGovernmentByUser(MainController.getCurrentUser());
        if (government.getTradeNotification().size() != 0) {
            AppMenu.show("notification: ");

            for (Trade trade : government.getTradeNotification())
                AppMenu.show(trade.getId() + ": resource_type:" + trade.getResourceType() + ", resource_amount "
                        + trade.getResourceAmount() + ", price: " + trade.getPrice() + ", requested_user: "
                        + trade.getApplicant().getUsername()
                        + ", message: " + trade.getMessage());

            government.getTradeNotification().clear();
        }

    }
}
