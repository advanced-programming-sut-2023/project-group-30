package controller.menu_controllers;

import controller.MainController;
import controller.messages.MenuMessages;
import model.game.Government;
import model.game.Trade;
import model.User;
import view.menus.AppMenu;

import java.util.ArrayList;

import static model.enums.Resource.getResourceByName;

public class TradeMenuController {
    public static MenuMessages trade(User receiver, String resource, int resourceAmount, int price, String message
            , Boolean request) {
        if (request) {
            if (getResourceByName(resource) == null)
                return MenuMessages.INVALID_RESOURCE;
            if (GameMenuController.getCurrentGame().getCurrentGovernment().getGold() < price)
                return MenuMessages.INVALID_MONEY;
            Government government = GameMenuController.getCurrentGame().getCurrentGovernment();
            if (government.getMaximumResource(government.getResourcesCategory(getResourceByName(resource))) <
                    (government.getStoredUnit(government.getResourcesCategory(getResourceByName(resource)))
                            + resourceAmount))
                return MenuMessages.NOT_ENOUGH_SPACE;
        } else {
            if (GameMenuController.getCurrentGame().getCurrentGovernment().getResources(getResourceByName(resource))
                    < resourceAmount)
                return MenuMessages.INVALID_AMOUNT;
        }

        Trade tradeItem = new Trade(GameMenuController.getCurrentGame().getCurrentGovernment().getOwner(), receiver
                , resourceAmount, getResourceByName(resource), price, message, request);

        addTradItem(tradeItem, GameMenuController.getCurrentGame().getCurrentGovernment().getOwner());
        return MenuMessages.OK;
    }


    public static void addTradItem(Trade tradeItem, User currentUser) {
        ArrayList<Government> governments = GameMenuController.getCurrentGame().getGovernments();
        for (int i = 0; i < governments.size(); i++) {
            if (!governments.get(i).getOwner().equals(currentUser)) {
                governments.get(i).addToTradeList(tradeItem);
            } else governments.get(i).addToTradeHistory(tradeItem);
        }
    }

    public static Government getGovernmentByUser(User currentUser) {
        ArrayList<Government> governments = GameMenuController.getCurrentGame().getGovernments();
        for (int i = 0; i < governments.size(); i++) {
            if (governments.get(i).getOwner().equals(currentUser)) {
                return governments.get(i);
            }
        }
        return null;

    }

    public static void showTradeList() {
        ArrayList<Trade> tradeList = GameMenuController.getCurrentGame().getCurrentGovernment().getTradeList();
        for (Trade trade : tradeList)
            AppMenu.show(trade.getId() + ": resource_type:" + trade.getResourceType() + ", resource_amount "
                    + trade.getResourceAmount() + ", price: " + trade.getPrice() + ", requested_user: "
                    + trade.getApplicant().getUsername() + ", message: " + trade.getMessage());
    }

    public static Trade getTradeById(int id) {
        ArrayList<Trade> tradeList = GameMenuController.getCurrentGame().getCurrentGovernment().getTradeList();
        for (Trade trade : tradeList) {
            if (trade.getId() == id)
                return trade;
        }
        return null;
    }

    public static MenuMessages tradeAccept(int id, String message) {
        Trade trade = getTradeById(id);
        Government government = GameMenuController.getCurrentGame().getCurrentGovernment();
        Government applicantGovernment = getGovernmentByUser(trade.getApplicant());

        if (trade.getResourceAmount() > government.getResources(trade.getResourceType()))
            return MenuMessages.INVALID_AMOUNT;

        government.addResources(trade.getResourceType(), -trade.getResourceAmount());

        removeFromTradList(trade.getId());

//        government.addToTradeHistory(new Trade(trade.getApplicant(), trade.getResourceAmount(),
//                trade.getResourceType(), trade.getPrice(), message));

        applicantGovernment.addGold(-trade.getPrice());
        government.addGold(trade.getPrice());


        return MenuMessages.OK;
    }

    public static void removeFromTradList(int id) {
        ArrayList<Government> governments = GameMenuController.getCurrentGame().getGovernments();
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
        User user = GameMenuController.getCurrentGame().getCurrentGovernment().getOwner();
        Government government = GameMenuController.getCurrentGame().getCurrentGovernment();
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
        Government government = GameMenuController.getCurrentGame().getCurrentGovernment();
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
