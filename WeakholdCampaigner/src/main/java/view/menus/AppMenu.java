package view.menus;

import view.Command;
import view.utils.GameUtils;
import view.utils.MenuUtils;

import java.util.ArrayList;
import java.util.Scanner;

public class AppMenu extends AbstractMenu {
    protected AppMenu(ArrayList<Command> commands, Scanner scanner, MenuName menuName) {
        super(commands, scanner, menuName);
    }

    public static AppMenu getMenu(MenuName menuName, Scanner scanner) {
        ArrayList<Command> commands = new ArrayList<>(getCommonCommands());

        if (menuName == MenuName.SIGNUP_MENU) {
            commands.add(new Command("user", "create", MenuUtils::userCreate));
            commands.add(new Command("enter", "login_menu", MenuUtils::enterLoginMenu));
        } else if (menuName == MenuName.LOGIN_MENU) {
            commands.add(new Command("user", "login", MenuUtils::userLogin));
            commands.add(new Command("forgot", "my_password", MenuUtils::forgotPassword));
            commands.add(new Command("enter", "signup_menu", MenuUtils::enterSignUpMenu));
        } else if (menuName == MenuName.PROFILE_MENU) {
            commands.add(new Command("profile", "change", MenuUtils::profileChange));
            commands.add(new Command("exit", "profile_menu", MenuUtils::enterMainMenu));
            commands.add(new Command("profile", "remove", MenuUtils::profileRemove));
            commands.add(new Command("profile", "display", MenuUtils::profileDisplay));
        } else if (menuName == MenuName.GAME_MENU) {
            commands.add(new Command("show", "map", GameUtils::showMap));
            commands.add(new Command("show", "popularity", GameUtils::showPopularity));
            commands.add(new Command("show", "food_list", GameUtils::showFoodList));
            commands.add(new Command("food", "rate", GameUtils::setFoodRate));
            commands.add(new Command("show", "food_rate", GameUtils::showFoodRate));
            commands.add(new Command("tax", "rate", GameUtils::taxRate));
            commands.add(new Command("show", "tax_rate", GameUtils::showTaxRate));
            commands.add(new Command("fear", "rate", GameUtils::fearRate));
            commands.add(new Command("drop", "building", GameUtils::dropBuilding));
            commands.add(new Command("select", "building", GameUtils::selectBuilding));
            commands.add(new Command("select", "unit", GameUtils::selectUnit));
            commands.add(new Command("drop", "unit", GameUtils::dropUnit));
            commands.add(new Command("exit", "game_menu", MenuUtils::enterMainMenu));
            commands.add(new Command("enter", "trade_menu", GameUtils::enterTradMenu));
            commands.add(new Command("enter", "shop_menu", GameUtils::enterShopMenu));
            commands.add(new Command("end", "turn", GameUtils::endTurn));
            commands.add(new Command("whose", "turn", GameUtils::whoseTurnIsIt));
            commands.add(new Command("which", "turn", GameUtils::whichTurnIsIt));
        } else if (menuName == MenuName.MAP_MENU) {
            commands.add(new Command("move", "map", GameUtils::moveMap));
            commands.add(new Command("show", "details", GameUtils::showDetails));
            commands.add((new Command("exit", "map_menu", GameUtils::exitFromMapMenu)));
        } else if (menuName == MenuName.MAIN_MENU) {
            commands.add(new Command("user", "logout", MenuUtils::userLogout));
            commands.add(new Command("create", "game", GameUtils::createGame));
            commands.add(new Command("enter", "game", GameUtils::enterGame));
            commands.add(new Command("enter", "profile_menu", MenuUtils::enterProfileMenu));
        } else if (menuName == MenuName.TRAD_MENU) {
            commands.add(new Command("exit", "trade_menu", GameUtils::exitTradMenu));
            commands.add(new Command("trade", "list", GameUtils::tradeList));
            commands.add(new Command("trade", "accept", GameUtils::tradeAccept));
            commands.add(new Command("trade", "history", GameUtils::tradeHistory));
            commands.add(new Command("trade", null, GameUtils::trade));
        } else if (menuName == MenuName.SHOP_MENU) {
            commands.add(new Command("exit", "shop_menu", GameUtils::exitShopMenu));
            commands.add(new Command("show", "price_list", GameUtils::showPriceList));
            commands.add(new Command("buy", null, GameUtils::buyItem));
            commands.add(new Command("sell", null, GameUtils::sellItem));

        }

        commands.add(new Command("save_and_exit", null, MenuUtils::saveAndExit));

        return new AppMenu(commands, scanner, menuName);
    }

    public static String getOneLine(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }
}
