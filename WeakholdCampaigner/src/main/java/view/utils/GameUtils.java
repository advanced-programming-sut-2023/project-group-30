package view.utils;

import controller.MainController;
import controller.menu_controllers.GameMenuController;
import controller.menu_controllers.MapController;
import controller.messages.MenuMessages;
import view.AppMenu;
import view.ParsedLine;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameUtils {


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
        if (checkStrIsNumberAndNotNullForAllEnteranc(X, Y)) {
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

    public static boolean checkStrIsNumberAndNotNullForAllEnteranc(String... enterances) {
        Pattern patternForCheckStrIsNumber = Pattern.compile("-?\\d+$");
        for (String enteranc : enterances) {
            if (enteranc == null)
                return false;
            if (!patternForCheckStrIsNumber.matcher(enteranc).matches())
                return false;
        }
        return true;
    }

    public static boolean checkStrIsNumberAndNotNullForMove(String... enterances) {
        int size = enterances.length;
        Pattern patternForCheckStrIsNumber = Pattern.compile("-?\\d+$");
        for (String enteranc : enterances) {
            if (enteranc == null)
                size--;
            else if (!patternForCheckStrIsNumber.matcher(enteranc).matches())
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
        if (checkStrIsNumberAndNotNullForAllEnteranc(X, Y)) {
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
            if (checkStrIsNumberAndNotNullForAllEnteranc(Rate)) {
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
            if (checkStrIsNumberAndNotNullForAllEnteranc(Rate)) {
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
            if (checkStrIsNumberAndNotNullForAllEnteranc(Rate)) {
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
        String X = null, Y = null, type = null;
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
                case "--type":
                    type = argument;
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "drop building -x [x] -y [y] --type [type]");
                    return;
            }
        }
        if (checkStrIsNumberAndNotNullForAllEnteranc(X, Y) && type != null) {
            int x = Integer.parseInt(X), y = Integer.parseInt(Y);
            switch (GameMenuController.dropBuilding(x, y, type)) {
                case OK:
                    break;
                case INVALID_LOCATION:
                    System.out.println("Error: please enter valid location");
                    break;

            }
        } else System.out.println("Error: please enter location or type correctly");
    }
    public static void selectBuilding(ParsedLine parsedLine) {
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
                            "select building -x [x] -y [y]");
                    return;
            }
        }
        if (checkStrIsNumberAndNotNullForAllEnteranc(X, Y)) {
            int x = Integer.parseInt(X), y = Integer.parseInt(Y);
            switch (GameMenuController.selectBuilding(x, y)) {
                case OK:
                    break;
                case INVALID_LOCATION:
                    System.out.println("Error: please enter valid location");
                    break;

            }
        } else System.out.println("Error: please enter location correctly");
    }
    public static void creatUnit(ParsedLine parsedLine) {
        String type = null, Count = null;
        HashMap<String, String> options = parsedLine.options;
        for (Map.Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "--type":
                    type = argument;
                    break;
                case "-c":
                    Count = argument;
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "create unit --type [type] -c [count]");
                    return;
            }
        }
        if (checkStrIsNumberAndNotNullForAllEnteranc(Count) && type != null) {
            int count = Integer.parseInt(Count);
            switch (GameMenuController.creatUnit(count,type)) {
                case OK:
                    break;
            }

        } else System.out.println("Error: pleas enter counter or type correctly");

    }
    public static void repair(ParsedLine parsedLine) {
        switch (GameMenuController.repair()) {
            case OK:
                break;
        }
    }




}
