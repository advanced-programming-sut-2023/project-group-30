package view.utils;

import controller.MainController;
import controller.menu_controllers.GameEntityController;
import view.ParsedLine;
import view.menus.AbstractMenu;

import java.util.HashMap;

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

        switch (GameEntityController.moveUnitTo(
                Integer.parseInt(options.get("-x")), Integer.parseInt(options.get("-y"))
        )) {
            case INVALID_LOCATION:
                AbstractMenu.show("Error: Location out of bounds.");
                break;
            case CELL_HAS_INCOMPATIBLE_TEXTURE:
                AbstractMenu.show("Error: The destination is unreachable due to its texture.");
                break;
            case IS_PATROLLING:
                AbstractMenu.show("Error: The Unit is currently patrolling.");
                AbstractMenu.show("You can use 'unit halt' to end its patrol.");
                break;
            case SUCCESS:
                AbstractMenu.show("Unit moved successfully.");
                break;
        }
    }

    public static void patrolUnit(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{"-x1", "-y1", "-x2", "-y2"},
                new String[]{},
                new String[]{"-x1", "-y1", "-x2", "-y2"});

        if (options == null) {
            invalidFormatError("unit patrol -x1 <x1> -y1 <y1> -x2 <x2> -y2 <y2>");
            return;
        }

        switch (GameEntityController.patrolUnit(
                Integer.parseInt(options.get("-x1")),
                Integer.parseInt(options.get("-y1")),
                Integer.parseInt(options.get("-x2")),
                Integer.parseInt(options.get("-y2")))
        ) {
            case INVALID_LOCATION:
                AbstractMenu.show("Error: At least one location is out of bounds.");
                break;
            case CELL_HAS_INCOMPATIBLE_TEXTURE:
                AbstractMenu.show("Error: At least one destination is unreachable due to its texture.");
                break;
            case SUCCESS:
                AbstractMenu.show("Success. Unit will start patrolling at the end of the turn.");
                AbstractMenu.show("You can use 'unit halt' to stop the unit's movement.");
                break;
        }
    }

    public static void halt(ParsedLine parsedLine) {
        GameEntityController.halt();

        AbstractMenu.show("Unit halted successfully.");
    }

    public static void setStance(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{"-s"}, new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("set stance -s <stance>");
            return;
        }

        GameEntityController.setStance(options.get("-s"));
    }

    public static void meleeAttack(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{"-x", "-y"}, new String[]{}, new String[]{"-x", "-y"});

        if (options == null) {
            invalidFormatError("attack melee -x <enemy's x> -y <enemy's y>");
            return;
        }

        GameEntityController.meleeAttack(Integer.parseInt(options.get("-x")),
                Integer.parseInt(options.get("-y")));
    }

    public static void rangedAttack(ParsedLine parsedLine) { //smelly. merge this with meleeAttack.
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{"-x", "-y"}, new String[]{}, new String[]{"-x", "-y"});

        if (options == null) {
            invalidFormatError("attack ranged -x <enemy's x> -y <enemy's y>");
            return;
        }

        GameEntityController.rangedAttack(Integer.parseInt(options.get("-x")),
                Integer.parseInt(options.get("-y")));
    }

    public static void pourOil(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{"-d"}, new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("pour oil -d <direction>");
            return;
        }

        GameEntityController.pourOil(options.get("-d"));
    }

    public static void digTunnel(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{"-x", "-y"}, new String[]{}, new String[]{"-x", "-y"});

        if (options == null) {
            invalidFormatError("dig tunnel -x <x> -y <y>");
            return;
        }

        GameEntityController.digTunnel(Integer.parseInt(options.get("-x")),
                Integer.parseInt(options.get("-y")));
    }

    public static void buildEquipment(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{"-q"}, new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("build equipment -q <equipment name>");
            return;
        }

        GameEntityController.buildEquipment(options.get("-q"));
    }

    public static void disbandUnit(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{}, new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("unit disband");
            return;
        }

        GameEntityController.disbandUnit();
    }

    public static void exitEntityMenu(ParsedLine parsedLine) {
        MainController.setCurrentMenu(AbstractMenu.MenuName.GAME_MENU);
        System.out.println("Entered Game Menu.");
    }


    public static void serveDrink(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{"-a"}, new String[]{}, new String[]{"-a"});

        if (options == null) {
            invalidFormatError("serve drink -a <amount>");
            return;
        }
        switch (GameEntityController.serveDrink(Integer.parseInt(options.get("-a")))) {
            case NOT_ENOUGH_RESOURCES:
                System.out.println("you don't have enough wine");
                break;
            case OK:
                break;
        }
    }

    public static void process(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{"-a"}, new String[]{}, new String[]{"-a"});

        if (options == null) {
            invalidFormatError("process -a <amount>");
            return;
        }
        switch (GameEntityController.process(Integer.parseInt(options.get("-a")))) {
            case NOT_ENOUGH_RESOURCES:
                System.out.println("you don't have this amount");
                break;
            case OK:
                break;
        }
    }
}
