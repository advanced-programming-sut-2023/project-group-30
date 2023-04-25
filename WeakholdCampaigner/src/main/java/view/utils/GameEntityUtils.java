package view.utils;

import controller.MainController;
import controller.menu_controllers.GameEntityController;
import view.ParsedLine;
import view.menus.AppMenu;

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

        GameEntityController.moveUnitTo(Integer.parseInt(options.get("-x")), Integer.parseInt(options.get("-y")));
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

        GameEntityController.patrolUnit(
                Integer.parseInt(options.get("-x1")),
                Integer.parseInt(options.get("-y1")),
                Integer.parseInt(options.get("-x2")),
                Integer.parseInt(options.get("-y2")));
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

    public static void exitEntityMenu(ParsedLine parsedLine){
        MainController.setCurrentMenu(AppMenu.MenuName.GAME_MENU);
        System.out.println("Entered Game Menu.");
    }
}
