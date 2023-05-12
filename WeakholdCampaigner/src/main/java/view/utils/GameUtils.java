package view.utils;

import controller.MainController;
import model.game.game_entities.Building;
import model.game.game_entities.BuildingName;
import model.game.game_entities.Unit;
import model.game.game_entities.UnitName;
import model.game.map.MapCell;
import controller.menu_controllers.GameMenuController;
import controller.menu_controllers.MapController;
import controller.menu_controllers.ShopMenuController;
import controller.menu_controllers.TradeMenuController;
import view.menus.AbstractMenu;
import view.menus.AppMenu; //TODO: it is better to put AbstractMenu instead ?
import view.ParsedLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class GameUtils extends Utils {
    public static void createGame(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"--mapID"}, new String[]{},
                new String[]{"--mapID"});

        if (options == null) {
            invalidFormatError("create game --mapID <map ID>");
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
                AppMenu.show("Error: No map with this id exists.\n Your chosen map id should be between 1 to 5");
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

    public static String setTextureColor(MapCell.Texture texture, String input) {
        final String ANSI_BLACK_BACKGROUND = "\u001B[40m";// SLATE
        final String ANSI_GREEN_BACKGROUND = "\u001B[42m";// Meadow
        final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";// GRASSLAND
        final String ANSI_BLUE_BACKGROUND = "\u001B[44m";// DEEP_WATER
        final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";// GRAVEL
        final String ANSI_GRAY_BACKGROUND = "\u001B[47m";// IRON
        final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// STONE
        final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// PlAIN
        final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GRASS
        final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// LAND
        final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// SHALLOW_WATER
        final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // OIL
        final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // RIVER
        final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // Beach
        final String ANSI_RESET = "\u001B[0m";
        switch (texture) {
            case MEADOW:
                return ANSI_GREEN_BACKGROUND + input + ANSI_RESET;
            case LAND:
                return YELLOW_BACKGROUND_BRIGHT + input + ANSI_RESET;
            case GRAVEL:
                return ANSI_PURPLE_BACKGROUND + input + ANSI_RESET;
            case SLATE:
                return ANSI_BLACK_BACKGROUND + input + ANSI_RESET;
            case STONE:
                return BLACK_BACKGROUND_BRIGHT + input + ANSI_RESET;
            case IRON:
                return ANSI_GRAY_BACKGROUND + input + ANSI_RESET;
            case GRASS:
                return GREEN_BACKGROUND_BRIGHT + input + ANSI_RESET;
            case GRASSLAND:
                return ANSI_YELLOW_BACKGROUND + input + ANSI_RESET;
            case SHALLOW_WATER:
                return BLUE_BACKGROUND_BRIGHT + input + ANSI_RESET;
            case DEEP_WATER:
                return ANSI_BLUE_BACKGROUND + input + ANSI_RESET;
            case RIVER:
                return CYAN_BACKGROUND_BRIGHT + input + ANSI_RESET;
            case BEACH:
                return WHITE_BACKGROUND_BRIGHT + input + ANSI_RESET;
            case PLAIN:
                return RED_BACKGROUND_BRIGHT + input + ANSI_RESET;
            case OIL:
                return PURPLE_BACKGROUND_BRIGHT + input + ANSI_RESET;
            default:
                return ANSI_RESET;
        }
    }

    public static void printMapCellComponents(int x, int y) {
        if (GameMenuController.getCurrentGame().getUnits(x, y).size() != 0 &&
                isAnyUnitMoving(GameMenuController.getCurrentGame().getUnits(x, y))) {
            System.out.print(setTextureColor
                    (GameMenuController.getCurrentGame().getTexture(x, y), "S"));
        } else if (GameMenuController.getCurrentGame().getBuilding(x, y) != null) {
            if (GameMenuController.getCurrentGame().getBuilding(x, y).getCategory()
                    != Building.Category.TREE) {
                System.out.print(setTextureColor
                        (GameMenuController.getCurrentGame().getTexture(x, y), "B"));
            } else {
                System.out.print(setTextureColor
                        (GameMenuController.getCurrentGame().getTexture(x, y), "T"));
            }
        } else if (GameMenuController.getCurrentGame().getUnits(x, y).size() != 0) {
            System.out.print(setTextureColor
                    (GameMenuController.getCurrentGame().getTexture(x, y), "U"));
        } else {
            System.out.print(setTextureColor
                    (GameMenuController.getCurrentGame().getTexture(x, y), " "));
        }
    }

    public static void printMap(int x, int y) {
        GameMenuController.getCurrentGame().setMapXPosition(x);
        GameMenuController.getCurrentGame().setMapYPosition(y);
        int row = 20;
        int column = 40;
        for (int i = 0; i <= row; i++) {
            for (int j = 0; j <= column; j++) {
                if (i % 4 == 0) {
                    System.out.print("-");
                } else if (j % 8 == 0) {
                    System.out.print("|");
                } else if (16 < j && j < 24 && i > 8 && i < 12) {
                    if (i % 20 == 10 && j % 40 == 20) {
                        printMapCellComponents(x, y);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x, y), " "));
                    }
                } else if (16 < j && j < 24 && i > 4 && i < 8) {
                    if (i % 20 == 6 && j % 40 == 20) {
                        printMapCellComponents(x - 1, y);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x - 1, y), " "));
                    }
                } else if (16 < j && j < 24 && i > 0 && i < 4) {
                    if (i % 20 == 2 && j % 40 == 20) {
                        printMapCellComponents(x - 2, y);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x - 2, y), " "));
                    }
                } else if (16 < j && j < 24 && i > 12 && i < 16) {
                    if (i % 20 == 14 && j % 40 == 20) {
                        printMapCellComponents(x + 1, y);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x + 1, y), " "));
                    }
                } else if (16 < j && j < 24 && i > 16) {
                    if (i % 20 == 18 && j % 40 == 20) {
                        printMapCellComponents(x + 2, y);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x + 2, y), " "));
                    }
                } else if (8 < j && j < 16 && i > 0 && i < 4) {
                    if (i % 20 == 2 && j % 40 == 12) {
                        printMapCellComponents(x - 2, y - 1);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x - 2, y - 1), " "));
                    }
                } else if (8 < j && j < 16 && i > 4 && i < 8) {
                    if (i % 20 == 6 && j % 40 == 12) {
                        printMapCellComponents(x - 1, y - 1);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x - 1, y - 1), " "));
                    }
                } else if (8 < j && j < 16 && i > 8 && i < 12) {
                    if (i % 20 == 10 && j % 40 == 12) {
                        printMapCellComponents(x, y - 1);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x, y - 1), " "));
                    }
                } else if (8 < j && j < 16 && i > 12 && i < 16) {
                    if (i % 20 == 14 && j % 40 == 12) {
                        printMapCellComponents(x + 1, y - 1);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x + 1, y - 1), " "));
                    }
                } else if (8 < j && j < 16 && i > 16) {
                    if (i % 20 == 18 && j % 40 == 12) {
                        printMapCellComponents(x + 2, y - 1);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x + 2, y - 1), " "));
                    }
                } else if (0 < j && j < 8 && i > 0 && i < 4) {
                    if (i % 20 == 2 && j % 40 == 4) {
                        printMapCellComponents(x - 2, y - 2);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x - 2, y - 2), " "));
                    }
                } else if (0 < j && j < 8 && i > 4 && i < 8) {
                    if (i % 20 == 6 && j % 40 == 4) {
                        printMapCellComponents(x - 1, y - 2);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x - 1, y - 2), " "));
                    }
                } else if (0 < j && j < 8 && i > 8 && i < 12) {
                    if (i % 20 == 10 && j % 40 == 4) {
                        printMapCellComponents(x, y - 2);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x, y - 2), " "));
                    }
                } else if (0 < j && j < 8 && i > 12 && i < 16) {
                    if (i % 20 == 14 && j % 40 == 4) {
                        printMapCellComponents(x + 1, y - 2);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x + 1, y - 2), " "));
                    }
                } else if (0 < j && j < 8 && i > 16) {
                    if (i % 20 == 18 && j % 40 == 4) {
                        printMapCellComponents(x + 2, y - 2);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x + 2, y - 2), " "));
                    }
                } else if (24 < j && j < 32 && i > 0 && i < 4) {
                    if (i % 20 == 2 && j % 40 == 28) {
                        printMapCellComponents(x - 2, y + 1);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x - 2, y + 1), " "));
                    }
                } else if (24 < j && j < 32 && i > 4 && i < 8) {
                    if (i % 20 == 6 && j % 40 == 28) {
                        printMapCellComponents(x - 1, y + 1);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x - 1, y + 1), " "));
                    }
                } else if (24 < j && j < 32 && i > 8 && i < 12) {
                    if (i % 20 == 10 && j % 40 == 28) {
                        printMapCellComponents(x, y + 1);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x, y + 1), " "));
                    }
                } else if (24 < j && j < 32 && i > 12 && i < 16) {
                    if (i % 20 == 14 && j % 40 == 28) {
                        printMapCellComponents(x + 1, y + 1);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x + 1, y + 1), " "));
                    }
                } else if (24 < j && j < 32 && i > 16) {
                    if (i % 20 == 18 && j % 40 == 28) {
                        printMapCellComponents(x + 2, y + 1);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x + 2, y + 1), " "));
                    }
                } else if (32 < j && i > 0 && i < 4) {
                    if (i % 20 == 2 && j % 40 == 36) {
                        printMapCellComponents(x - 2, y + 2);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x - 2, y + 2), " "));
                    }
                } else if (32 < j && i > 4 && i < 8) {
                    if (i % 20 == 6 && j % 40 == 36) {
                        printMapCellComponents(x - 1, y + 2);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x - 1, y + 2), " "));
                    }
                } else if (32 < j && i > 8 && i < 12) {
                    if (i % 20 == 10 && j % 40 == 36) {
                        printMapCellComponents(x, y + 2);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x, y + 2), " "));
                    }
                } else if (32 < j && i > 12 && i < 16) {
                    if (i % 20 == 14 && j % 40 == 36) {
                        printMapCellComponents(x + 1, y + 2);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x + 1, y + 2), " "));
                    }
                } else if (32 < j && i > 16) {
                    if (i % 20 == 18 && j % 40 == 36) {
                        printMapCellComponents(x + 2, y + 2);
                    } else {
                        System.out.print(setTextureColor
                                (GameMenuController.getCurrentGame().getTexture(x + 2, y + 2), " "));
                    }
                }
            }
            System.out.println();
        }
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
            switch (MapController.showMap(x, y)) {
                case OK:
                    MainController.setCurrentMenu(AppMenu.MenuName.MAP_MENU);
                    printMap(x, y);
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

            switch (MapController.moveMap(Right, Left, Up, Down, GameMenuController.getCurrentGame().getMapXPosition(),
                    GameMenuController.getCurrentGame().getMapYPosition())) {
                case OK:
                    int newX = GameMenuController.getCurrentGame().getMapXPosition() + Down - Up;
                    int newY = GameMenuController.getCurrentGame().getMapYPosition() + Right - Left;
                    printMap(newX, newY);
                    break;
                case INVALID_LOCATION:
                    System.out.println("Your location is wrong");
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

    public static boolean isAnyUnitMoving(ArrayList<Unit> units) {
        for (Unit unit : units) {
            if (unit.isMoving()) {
                return true;
            }
        }
        return false;
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
                    System.out.println("Texture : " + GameMenuController.getCurrentGame().getTexture(x, y));
                    if (GameMenuController.getCurrentGame().getUnits(x, y) != null) {
                        System.out.println("Unit types are :");
                        for (Unit unit : GameMenuController.getCurrentGame().getUnits(x, y)) {
                            System.out.println(unit.getUnitName());
                        }
                    }
                    if (GameMenuController.getCurrentGame().getUnits(x, y) != null) {
                        System.out.println("Number of units : " + GameMenuController.getCurrentGame().getUnits(x, y).size());
                    } else {
                        System.out.println("Number of units : 0");
                    }
                    if (GameMenuController.getCurrentGame().getBuilding(x, y) != null) {
                        System.out.println("Building is : "
                                + GameMenuController.getCurrentGame().getBuilding(x, y).getBuildingName());
                    } else {
                        System.out.println("There is no building on this cell");
                    }
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
            System.out.println(GameMenuController.showPopularity());
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
                        System.out.println("set food rate");
                        break;
                    case OUT_OF_BOUNDS:
                        System.out.println("food rate must be between 2 & -2");
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
                        System.out.println("set tax rate");
                        break;
                    case OUT_OF_BOUNDS:
                        System.out.println("tax rate must be between 8 & -3");
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
                        System.out.println("set fear rate");
                        break;
                    case OUT_OF_BOUNDS:
                        System.out.println("fear rate must be between 5 & -5");
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
            invalidFormatError("select unit -x <x> -y <y> --type <unit type>");
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

    public static void dropUnit(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-x", "-y", "-t"},
                new String[]{"--godMode"}, new String[]{"-x", "-y"});

        if (options == null) {
            invalidFormatError("drop unit -x <x> -y <y> -t <type> [--godMode <on>]");
            return;
        }

        String tempGodMode = options.get("--godMode");
        boolean isFree = tempGodMode != null && tempGodMode.equals("on");

        switch (
                GameMenuController.dropUnit(Integer.parseInt(options.get("-x")),
                        Integer.parseInt(options.get("-y")),
                        options.get("-t"),
                        isFree)
        ) {
            case INVALID_LOCATION:
                AppMenu.show("Error: Location is out of bounds.");
                break;
            case INVALID_TYPE:
                AppMenu.show("Error: Invalid unit type."); //TODO: show all available units
                break;
            case CELL_HAS_INCOMPATIBLE_TEXTURE:
                AppMenu.show("Error: The cell has an incompatible texture.");
                break;
            case NOT_ENOUGH_RESOURCES:
                AppMenu.show("Error: Your government does not have enough resources.");
                AppMenu.show("You can drop this unit for free using the '--godMode on' flag.");
            case SUCCESS:
                AppMenu.show("Unit dropped successfully.");
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
            case NOT_ENOUGH_SPACE:
                System.out.println("not enough space");
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

    public static void buyItem(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-i", "-a"}
                , new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("buy -i <item’s name> -a <item’s amount>");
            return;
        }
        switch (ShopMenuController.buyItem(Integer.parseInt(options.get("-a")), options.get("-i"))) {
            case INVALID_RESOURCE:
                System.out.println("shop doesn't have this resource");
                break;
            case INVALID_AMOUNT:
                System.out.println("shop doesn't have enough amount of this resource");
                break;
            case INVALID_MONEY:
                System.out.println("you doesn't have enough money for buy this item");
                break;
            case INVALID_COMMAND:
                System.out.println("error: you entered your confirmation incorrect");
                break;
            case NOT_ENOUGH_SPACE:
                System.out.println("not enough space");
                break;
            case CANCEL:
                System.out.println("your purchase canceled successfully");
                break;
            case OK:
                System.out.println("you purchased successfully");
                break;
        }

    }

    public static void sellItem(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-i", "-a"}
                , new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("sell -i <item’s name> -a <item’s amount>");
            return;
        }
        switch (ShopMenuController.sellItem(Integer.parseInt(options.get("-a")), options.get("-i"))) {
            case INVALID_RESOURCE:
                System.out.println("you doesn't have this resource");
                break;
            case INVALID_AMOUNT:
                System.out.println("you doesn't have enough amount");
                break;
            case CANCEL:
                System.out.println("you successfully canceled this item");
                break;
            case INVALID_COMMAND:
                System.out.println("error: you entered your confirmation incorrect");
                break;
            case OK:
                System.out.println("you sold successfully");
                break;


        }

    }


    public static void setCellTexture(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-x", "-y", "-t"}
                , new String[]{}, new String[]{"-x", "-y"});
        if (options == null) {
            invalidFormatError("set cell_texture -x [x] -y [y] -t <type of new texture>");
            return;
        }
        switch (MapController.setCellTexture(Integer.parseInt(options.get("-x")), Integer.parseInt(options.get("-y")),
                options.get("-t"))) {
            case OK:
                System.out.println("Done!");
                break;
            case BUILDING_EXISTENCE:
                System.out.println("There is a building on this tile!");
                break;
            case INVALID_LOCATION:
                System.out.println("Wrong location");
                break;
            case INVALID_TYPE:
                System.out.println("Your texture type is incorrect.\n The correct types are : land, gravel, slate," +
                        "stone, iron, grass, grassland, meadow, shallow_water, deep_water, oil, beach, river, plain");
        }
    }

    public static void setBlockTexture(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-x", "-y", "-x2", "-y2", "-t"}
                , new String[]{}, new String[]{"-x", "-y", "-x2", "-y2"});
        if (options == null) {
            invalidFormatError("set block_texture -x [x] -y [y] -x2 [x2] -y2 [y2] -t <type of new texture of block>");
            return;
        }
        switch (MapController.setBlockTexture(Integer.parseInt(options.get("-x")), Integer.parseInt(options.get("-y")),
                Integer.parseInt(options.get("-x2")), Integer.parseInt(options.get("-y2")), options.get("-t"))) {
            case BUILDING_EXISTENCE:
                System.out.println("There is a building on these tiles!");
                break;
            case INVALID_LOCATION:
                System.out.println("Wrong location");
                break;
            case INVALID_TYPE:
                System.out.println("Your texture type is incorrect.\n The correct types are : land, gravel, slate," +
                        "stone, iron, grass, grassland, meadow, shallow_water, deep_water, oil, beach, river, plain");
                break;
            case OK:
                System.out.println("Done!");
                break;
        }
    }

    public static void clear(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-x", "-y"}
                , new String[]{}, new String[]{"-x", "-y"});
        if (options == null) {
            invalidFormatError("clear -x [x] -y [y]");
            return;
        }
        switch (MapController.clear(Integer.parseInt(options.get("-x")), Integer.parseInt(options.get("-y")))) {
            case OK:
                System.out.println("Done!");
                break;
            case INVALID_LOCATION:
                System.out.println("Wrong location");
                break;
        }
    }

    public static void dropRock(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-x", "-y", "-d"}
                , new String[]{}, new String[]{"-x", "-y"});
        if (options == null) {
            invalidFormatError("drop rock -x [x] -y [y] -d <direction of the rock>");
            return;
        }
        switch (MapController.dropRock(Integer.parseInt(options.get("-x")),
                Integer.parseInt(options.get("-y")), options.get("-d"))) {
            case INVALID_LOCATION:
                System.out.println("Wrong location");
                break;
            case OK:
                System.out.println("Done!");
                break;
            case INVALID_DIRECTION:
                System.out.println("Your direction should be between these character : r <random> , n, e, w, s");
                break;
            case BUILDING_EXISTENCE:
                System.out.println("There is a building on these tiles!");
                break;
        }
    }

    public static void dropTree(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-x", "-y", "-t"}
                , new String[]{}, new String[]{"-x", "-y"});
        if (options == null) {
            invalidFormatError("drop tree -x [x] -y [y] -t <type of tree>");
            return;
        }
        switch (MapController.dropTree(Integer.parseInt(options.get("-x")),
                Integer.parseInt(options.get("-y")), options.get("-t"))) {
            case BUILDING_EXISTENCE:
                System.out.println("There is a building on these tiles!");
                break;
            case INVALID_LOCATION:
                System.out.println("Wrong location");
                break;
            case INVALID_TYPE:
                System.out.println("Your tree type should be between desert_shrub," +
                        " olive_tree, cherry_tree, coconut_tree, date_tree");
                break;
            case OK:
                System.out.println("Done!");
                break;
            case CELL_HAS_INCOMPATIBLE_TEXTURE:
                System.out.println("The texture of cell is inappropriate for tree");
                break;
        }

    }

    public static void endTurn(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{}
                , new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("end turn");
            return;
        }

        GameMenuController.endOnePlayersTurn();
        AppMenu.show("Successful. It is next player's turn. type 'whose turn' to see who they are.");
    }

    public static void whoseTurnIsIt(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{}
                , new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("whose turn");
            return;
        }

        AbstractMenu.show("It is " + GameMenuController.whoseTurn() + "'s turn.");
    }

    public static void whichTurnIsIt(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{}
                , new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("which turn");
            return;
        }

        AbstractMenu.show("It is turn number " + GameMenuController.getTurn() + ".");
    }

    public static void showUnits(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{}
                , new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("show units");
            return;
        }

        for (UnitName unitName :
                UnitName.values()) {
            AbstractMenu.show(unitName.name);
        }
    }

    public static void showBuildings(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{}
                , new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("show buildings");
            return;
        }

        for (BuildingName buildingName :
                BuildingName.values()) {
            AbstractMenu.show(buildingName.name);
        }
    }
}
