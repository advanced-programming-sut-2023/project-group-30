package view.utils;

import controller.menu_controllers.GameEntityController;
import view.ParsedLine;

import java.util.HashMap;
import java.util.Map;

public class GameEntityUtils extends Utils {
    public static void createUnit(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-t", "-c"}, new String[]{},
                new String[]{"-c"});

        if (options == null) {
            invalidFormatError("create unit -t <type> -c <count>");
            return;
        }

        GameEntityController.createUnit(options.get("-t"), Integer.parseInt(options.get("-c")));
    }

    public static void repair(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{}, new String[]{},
                new String[]{});

        if (options == null) {
            invalidFormatError("repair");
            return;
        }

        GameEntityController.repairBuilding();
    }

    public static void moveUnit(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-x", "-y"}, new String[]{},
                new String[]{"-x", "-y"});

        if (options == null) {
            invalidFormatError("unit move_to -x <x> -y <y>");
            return;
        }

        GameEntityController.moveUnit(Integer.parseInt(options.get("-x")), Integer.parseInt(options.get("-y")));
    }

    public static void setStance(ParsedLine parsedLine) {
        String x = null, y = null, stance = null;
        boolean invalidOption = false;

        HashMap<String, String> options = parsedLine.options;
        for (Map.Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "-x":
                    x = argument;
                    break;
                case "-y":
                    y = argument;
                    break;
                case "-s":
                    stance = argument;
                    break;
                default:
                    invalidOption = true;
                    break;
            }
        }

        if (invalidOption || hasNullString(x, y, stance) || !areIntegersOrNull(x, y)) {
            invalidFormatError("set stance -x <x> -y <y> -s <stance>");
            return;
        }
        assert x != null;
        assert y != null;

        GameEntityController.setStance(Integer.parseInt(x), Integer.parseInt(y), stance);
    }

    public static void attack(ParsedLine parsedLine) {

    }
}
