package view.utils;

import controller.MainController;
import controller.menu_controllers.GameEntityController;
import view.ParsedLine;
import view.menus.AbstractMenu;

import java.util.HashMap;

public class GameEntityUtils extends Utils {
    public static void createUnit(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-t"}, new String[]{},
                new String[]{});

        if (options == null) {
            invalidFormatError("create unit -t <type>");
            return;
        }

        switch (GameEntityController.createUnit(options.get("-t"))) {
            case INVALID_TYPE:
                AbstractMenu.show("Error: There is no unit with this name.");
                break;
            case INVALID_RACE:
                AbstractMenu.show("Error: You cannot create units of that race in this building.");
                break;
            case INVALID_AMOUNT:
                AbstractMenu.show("Error: You do not have enough resources to create that unit.");
                break;
            case SUCCESS:
                AbstractMenu.show("Unit created successfully.");
                break;
        }
    }

    public static void repair(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{}, new String[]{"--godMode"},
                new String[]{});

        if (options == null) {
            invalidFormatError("repair [--godMode <on>]");
            return;
        }

        boolean isGodMode;
        String godMode = options.get("--godMode");
        isGodMode = godMode != null && godMode.equals("on");

        if (GameEntityController.repairBuilding(isGodMode)) AbstractMenu.show("Repaired successfully.");
        else AbstractMenu.show("Error: You do not have enough GoldCoin.");
    }

    public static void damageBuilding(ParsedLine parsedLine) {
        //remove this method
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{"-d"}, new String[]{},
                new String[]{"-d"});

        if (options == null) {
            invalidFormatError("damage -d <damage>");
            return;
        }

        if (GameEntityController.damageBuilding(Integer.parseInt(options.get("-d"))))
            AbstractMenu.show("Building destroyed.");
        else AbstractMenu.show("Building damaged.");
    }

    public static void showBuildingHealth(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{}, new String[]{},
                new String[]{});

        if (options == null) {
            invalidFormatError("show health");
            return;
        }

        AbstractMenu.show("HP = " + GameEntityController.getBuildingHealth());
    }

    public static void showUnitHealth(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(parsedLine.options, new String[]{}, new String[]{},
                new String[]{});

        if (options == null) {
            invalidFormatError("show health");
            return;
        }

        AbstractMenu.show("HP = " + GameEntityController.getUnitHealth());
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
            case NO_REMAINING_MOVEMENT:
                AbstractMenu.show("Error: This unit does not have any remaining movement.");
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

        switch (GameEntityController.setStance(options.get("-s"))) {
            case INVALID_TYPE:
                AbstractMenu.show("Error: " +
                        "The unit stance can be either 'standing', 'defensive' or 'offensive'.");
                break;
            case SUCCESS:
                AbstractMenu.show("Unit stance set successfully.");
                break;
        }
    }

    public static void attack(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{"-x", "-y"}, new String[]{}, new String[]{"-x", "-y"});

        if (options == null) {
            if (parsedLine.subCommand.equals("melee"))
                invalidFormatError("attack melee -x <enemy's x> -y <enemy's y>");
            else
                invalidFormatError("attack ranged -x <enemy's x> -y <enemy's y>");
            return;
        }

        switch (GameEntityController.attack(
                Integer.parseInt(options.get("-x")),
                Integer.parseInt(options.get("-y")),
                parsedLine.subCommand.equals("ranged")
        )) {
            case ALREADY_ATTACKED:
                AbstractMenu.show("Error: This unit has already attacked in this turn.");
                break;
            case TOO_FAR:
                AbstractMenu.show("Error: The selected location is out of your unit's range.");
                break;
            case NO_MATCHING_UNIT:
                AbstractMenu.show("Error: No enemy unit in that location.");
                break;
            case SUCCESS:
                AbstractMenu.show("Your unit engaged in " + parsedLine.subCommand + " combat successfully.");
                break;
        }
    }

    public static void pourOil(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{"-d"}, new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("pour oil -d <direction>");
            return;
        }

        GameEntityController.pourOil(options.get("-d"));
        AbstractMenu.show("Oil poured successfully.");
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
        AbstractMenu.show("Tunnel got dug successfully.");
    }

    public static void buildEquipment(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{"-q"}, new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("build equipment -q <equipment name>");
            return;
        }

        GameEntityController.buildEquipment(options.get("-q"));
        AbstractMenu.show("Equipment built successfully.");
    }

    public static void doLadder(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{}, new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("do ladder");
            return;
        }

        //todo : GameEntityController.doLadder();
        AbstractMenu.show("Laddered successfully.");
    }

    public static void disbandUnit(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{}, new String[]{}, new String[]{});

        if (options == null) {
            invalidFormatError("unit disband");
            return;
        }

        GameEntityController.disbandUnit();
        AbstractMenu.show("Unit disbanded successfully.");
        exitEntityMenu(parsedLine); //may be bug prone
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


    public static void showCondition(ParsedLine parsedLine) {
        GameEntityController.showCondition();
    }

    public static void digMoat(ParsedLine parsedLine) {
        HashMap<String, String> options = formatOptions(
                parsedLine.options, new String[]{"-x", "-y"}, new String[]{}, new String[]{"-x", "-y"});

        if (options == null) {
            invalidFormatError("dig moat -x <x> -y <y>");
            return;
        }

        //todo
        AbstractMenu.show("Moat dug successfully.");
    }

}
