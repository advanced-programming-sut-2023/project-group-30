package view.utils;

import controller.MainController;
import controller.menu_controllers.GameMenuController;
import controller.menu_controllers.MapController;
import controller.menu_controllers.ShopMenuController;
import controller.menu_controllers.TradeMenuController;
import view.menus.AppMenu; //TODO: it is better to put AbstractMenu instead ?
import view.ParsedLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class GameUtils extends Utils {
    public static void createGame(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"--mapId"}, new String[]{},
                new String[]{"--mapId"});

        if (options == null) {
            invalidFormatError("create game --mapId <map id>");
            return;
        }

        AppMenu.show("Please enter the players' usernames below. Type !q when you are done.");
        ArrayList<String> usernames = new ArrayList<>();
        int i = 0;
        while (true) {
            String username = AppMenu.getOneLine("player" + (++i + 1) + ": ");
            if (username.equals("!q")) break;
            usernames.add(username);
        }

        switch (GameMenuController.createGame(Integer.parseInt(options.get("--mapID")), usernames)) {
            case MAP_DOES_NOT_EXIST:
                AppMenu.show("Error: No map with this id exists."); //TODO: show available map ids.
                break;
            case INVALID_NUMBER_OF_PLAYERS:
                AppMenu.show("Error: Each game can have 2 to 8 players, including you.");
                break;
            case USERNAME_DOES_NOT_EXIST:
                AppMenu.show("Error: At least one of the usernames does not exist.");
                break;
            case SUCCESS:
                AppMenu.show("Game created successfully. Use the above id to enter it.");
                break;
        }
    }

    public static void enterGame(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"--id"}, new String[]{},
                new String[]{"--id"});

        if (options == null) {
            invalidFormatError("enter game --id <game id>");
            return;
        }

        if (!GameMenuController.loadGame(Integer.parseInt(options.get("--id")))) {
            System.out.println("No game with this id exists.");
            return;
        }

        MainController.setCurrentMenu(AppMenu.MenuName.GAME_MENU);
        System.out.println("entered game_menu");
    }

    public static void showMap(ParsedLine parsedLine) {
        String X = null, Y = null;
        HashMap<String, String> options = parsedLine.options;
        for (Map.Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "-x":
                    X = argument;
                    break;
                case "-y":
                    Y = argument;
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "show map -x [x] -y [y]");
                    return;
            }
        }
        if (checkStrIsNumberAndNotNullForAllEntrance(X, Y)) {
            int x = Integer.parseInt(X), y = Integer.parseInt(Y);
            switch (GameMenuController.showMap(x, y)) {
                case OK:
                    MainController.setCurrentMenu(AppMenu.MenuName.MAP_MENU);
                    break;
                case INVALID_LOCATION:
                    System.out.println("Error: please enter valid location");
                    break;

            }
        } else System.out.println("Error: please enter location correctly");

    }

    public static void moveMap(ParsedLine parsedLine) {
        String right = null, left = null, up = null, down = null;
        HashMap<String, String> options = parsedLine.options;
        for (Map.Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "--left":
                    left = argument;
                    if (argument == null) left = "1";
                    break;
                case "--right":
                    right = argument;
                    if (argument == null) right = "1";
                    break;
                case "--up":
                    up = argument;
                    if (argument == null) up = "1";
                    break;
                case "--down":
                    down = argument;
                    if (argument == null) down = "1";
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "move map [--up [<numberOFMove>]] [--down [<numberOFMove>]] [--right [<numberOFMove>]]" +
                            "[--left [<numberOFMove>]]");
                    return;

            }
        }
        if (checkStrIsNumberAndNotNullForMove(right, left, up, down)) {
            int Right = changeStrToIntForMove(right), Left = changeStrToIntForMove(left), Up = changeStrToIntForMove(up),
                    Down = changeStrToIntForMove(down);

            switch (MapController.moveMap(Right, Left, Up, Down)) {
                case OK:
                    break;
            }
        } else System.out.println("Error: This command should have the following format:\n" +
                "move map [--up [<numberOFMove>]] [--down [<numberOFMove>]] [--right [<numberOFMove>]]" +
                "[--left [<numberOFMove>]]");
    }

    public static boolean checkStrIsNumberAndNotNullForAllEntrance(String... entrances) {
        Pattern patternForCheckStrIsNumber = Pattern.compile("-?\\d+$");
        for (String entrance : entrances) {
            if (entrance == null)
                return false;
            if (!patternForCheckStrIsNumber.matcher(entrance).matches())
                return false;
        }
        return true;
    }

    public static boolean checkStrIsNumberAndNotNullForMove(String... entrances) {
        int size = entrances.length;
        Pattern patternForCheckStrIsNumber = Pattern.compile("-?\\d+$");
        for (String entrance : entrances) {
            if (entrance == null)
                size--;
            else if (!patternForCheckStrIsNumber.matcher(entrance).matches())
                return false;
        }
        if (size == 0)
            return false;
        else return true;
    }

    public static int changeStrToIntForMove(String dir) {
        if (dir == null)
            return 0;
        else return Integer.parseInt(dir);
    }

    public static void showDetails(ParsedLine parsedLine) {
        String X = null, Y = null;
        HashMap<String, String> options = parsedLine.options;
        for (Map.Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "-x":
                    X = argument;
                    break;
                case "-y":
                    Y = argument;
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "show details -x [x] -y [y]");
                    return;
            }
        }
        if (checkStrIsNumberAndNotNullForAllEntrance(X, Y)) {
            int x = Integer.parseInt(X), y = Integer.parseInt(Y);
            switch (MapController.showDetails(x, y)) {
                case OK:
                    break;
                case INVALID_LOCATION:
                    System.out.println("Error: please enter valid location");
                    break;

            }
        } else System.out.println("Error: please enter location correctly");

    }

    public static void exitFromMapMenu(ParsedLine parsedLine) {
        MainController.setCurrentMenu(AppMenu.MenuName.GAME_MENU);
        System.out.println("you are in Game Menu");
    }

    public static void showPopularity(ParsedLine parsedLine) {
        HashMap<String, String> options = parsedLine.options;
        boolean signForFactors = false;
        for (Map.Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey();
            switch (option) {
                case "--factors":
                    signForFactors = true;
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "show popularity [--factors]");
                    return;
            }
        }
        if (signForFactors)
            GameMenuController.showPopularityFactor();
        else {
            System.out.println(GameMenuController.showPopularity(MainController.getCurrentUser()));
        }
    }

    public static void showFoodList(ParsedLine parsedLine) {
        GameMenuController.showFoodList();
    }

    public static void setFoodRate(ParsedLine parsedLine) {
        String Rate = getRate(parsedLine);
        if (Rate == null)
            System.out.println("Error: This command should have the following format:\n" +
                    "food rate -r <rateNumber>");
        else {
            if (checkStrIsNumberAndNotNullForAllEntrance(Rate)) {
                int rate = Integer.parseInt(Rate);
                switch (GameMenuController.foodRate(rate)) {
                    case OK:
                        break;
                }
            } else System.out.println("Error: please enter rateNumber correctly");

        }
    }

    public static void showFoodRate(ParsedLine parsedLine) {
        GameMenuController.showFoodRate();
    }

    public static void taxRate(ParsedLine parsedLine) {
        String Rate = getRate(parsedLine);
        if (Rate == null)
            System.out.println("Error: This command should have the following format:\n" +
                    "tax rate -r <rateNumber>");
        else {
            if (checkStrIsNumberAndNotNullForAllEntrance(Rate)) {
                int rate = Integer.parseInt(Rate);
                switch (GameMenuController.taxRate(rate)) {
                    case OK:
                        break;
                }
            } else System.out.println("Error: please enter rateNumber correctly");

        }
    }

    public static void showTaxRate(ParsedLine parsedLine) {
        GameMenuController.showTaxRate();
    }

    public static void fearRate(ParsedLine parsedLine) {
        String Rate = getRate(parsedLine);
        if (Rate == null)
            System.out.println("Error: This command should have the following format:\n" +
                    "fear rate -r <rateNumber>");
        else {
            if (checkStrIsNumberAndNotNullForAllEntrance(Rate)) {
                int rate = Integer.parseInt(Rate);
                switch (GameMenuController.setFearRate(rate)) {
                    case OK:
                        break;
                }
            } else System.out.println("Error: please enter rateNumber correctly");

        }
    }

    public static String getRate(ParsedLine parsedLine) {
        String Rate = null;
        HashMap<String, String> options = parsedLine.options;
        for (Map.Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "-r":
                    Rate = argument;
                    break;
                default:
                    return null;
            }
        }
        return Rate;
    }

    public static void dropBuilding(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-x", "-y", "--type"},
                new String[]{"--godMode"}, new String[]{"-x", "-y"});

        if (options == null) {
            invalidFormatError("drop building -x <x> -y <y> --type <type> [--godMode <on>]");
            return;
        }

        String tempGodMode = options.get("--godMode");
        boolean isFree = tempGodMode != null && tempGodMode.equals("on");

        switch (
                GameMenuController.dropBuilding(Integer.parseInt(options.get("-x")),
                Integer.parseInt(options.get("-y")),
                options.get("--type"),
                        isFree)
        ) {
            case INVALID_LOCATION:
                AppMenu.show("Error: Location is out of bounds.");
                break;
            case INVALID_TYPE:
                AppMenu.show("Error: Invalid building type."); //TODO: show all available buildings
                break;
            case ALREADY_HAS_KEEP:
                AppMenu.show("Error: You can have only one Keep.");
                break;
            case HAS_NOT_PLACED_KEEP:
                AppMenu.show("Error: You must Place your Keep before any other building.");
                break;
            case CELL_IS_FULL:
                AppMenu.show("Error: There is already a building in that location.");
                break;
            case CELL_HAS_INCOMPATIBLE_TEXTURE:
                AppMenu.show("Error: The cell has an incompatible texture.");
                break;
            case NOT_ENOUGH_RESOURCES:
                AppMenu.show("Error: Your government does not have enough resources.");
                AppMenu.show("You can drop this building for free using the '--godMode on' flag.");
            case SUCCESS:
                AppMenu.show("Building dropped successfully.");
        }
    }

    public static void selectBuilding(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-x", "-y"},
                new String[]{}, new String[]{"-x", "-y"});

        if (options == null) {
            invalidFormatError("select building -x <x> -y <y>");
            return;
        }

        switch (GameMenuController.selectBuilding(
                Integer.parseInt(options.get("-x")),
                Integer.parseInt(options.get("-y"))
        )) {
            case INVALID_LOCATION:
                AppMenu.show("Error: Location is out of bounds.");
                break;
            case CELL_IS_EMPTY:
                AppMenu.show("Error: There is no building in that cell.");
                break;
            case NOT_THE_OWNER:
                AppMenu.show("Error: You do not own that building.");
                break;
            case SUCCESS:
                AppMenu.show("Entered Entity Menu.");
                break;
        }
    }

    public static void selectUnit(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-x", "-y", "--type"},
                new String[]{}, new String[]{"-x", "-y"});

        if (options == null) {
            invalidFormatError("select building -x <x> -y <y> --type <unit type>");
            return;
        }

        String type = options.get("--type");
        switch (GameMenuController.selectUnit(
                Integer.parseInt(options.get("-x")),
                Integer.parseInt(options.get("-y")),
                type
        )) {
            case INVALID_LOCATION:
                AppMenu.show("Error: Location is out of bounds.");
                break;
            case INVALID_TYPE:
                AppMenu.show("Error: Invalid Unit type.");
                break;
            case NO_MATCHING_UNIT:
                AppMenu.show("Error: There is no unit of type <" + type + "> that belongs to you in that cell.");
                break;
            case SUCCESS:
                AppMenu.show("Entered Entity Menu.");
                break;
        }
    }

    public static void enterTradMenu(ParsedLine parsedLine) {
        TradeMenuController.enterTradeMenu();
    }

    public static void exitTradMenu(ParsedLine parsedLine) {
        MainController.setCurrentMenu(AppMenu.MenuName.GAME_MENU);
        System.out.println("entered game menu");
    }

    public static void trade(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-t", "-a", "-p", "-m"}
                , new String[]{}, new String[]{"-p", "-a"});

        if (options == null) {
            invalidFormatError("trade -t [resourceType] -a [resourceAmount] -p [price] -m [message]");
            return;
        }
        switch (TradeMenuController.trade(options.get("-t"), Integer.parseInt(options.get("-a")),
                Integer.parseInt(options.get("-p")), options.get("-m"))) {
            case INVALID_RESOURCE:
                System.out.println("error: resource is not correct");
                break;
            case INVALID_MONEY:
                System.out.println("you does not haven enough gold coin");
                break;
            case OK:
                System.out.println("your request/donation added");
                break;
        }
    }

    public static void tradeList(ParsedLine parsedLine) {
        TradeMenuController.showTradeList();
    }

    public static void tradeAccept(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-i", "-m"}
                , new String[]{}, new String[]{"-i"});

        if (options == null) {
            invalidFormatError("trade accept -i [id] -m message");
            return;
        }
        switch (TradeMenuController.tradeAccept(Integer.parseInt(options.get("-i")), options.get("-m"))) {
            case INVALID_AMOUNT:
                System.out.println("you does not have this amount of this resource");
                break;
            case OK:
                System.out.println("trade successful");

        }

    }

    public static void tradeHistory(ParsedLine parsedLine) {
        TradeMenuController.showTradeHistory();
    }

    public static void enterShopMenu(ParsedLine parsedLine) {
        MainController.setCurrentMenu(AppMenu.MenuName.SHOP_MENU);
        System.out.println("you entered shop menu");
    }

    public static void exitShopMenu(ParsedLine parsedLine) {
        MainController.setCurrentMenu(AppMenu.MenuName.GAME_MENU);
        System.out.println("you entered game menu");
    }

    public static void showPriceList(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{}
                , new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("show price_list");
            return;
        }
        ShopMenuController.showPriceList();
    }
}
