package view;

import controller.MainController;
import model.attributes.Attribute;
import model.attributes.building_attributes.CreateUnit;
import model.attributes.building_attributes.HasHP;
import model.attributes.unit_attributes.CloseCombat;
import model.game_entities.Building;
import model.game_entities.GameEntity;
import model.game_entities.Unit;
import view.utils.GameEntityUtils;
import view.utils.GameUtils;
import view.utils.MenuUtils;

import java.util.ArrayList;
import java.util.Scanner;

public class AppMenu {
    private final ArrayList<Command> commands = new ArrayList<>();
    private static Scanner scanner = null;

    private AppMenu(ArrayList<Command> commands, Scanner scanner) {
        this.scanner = scanner;
        this.commands.addAll(commands);
    }

    public static AppMenu getMenu(MenuName menuName, Scanner scanner) {
        ArrayList<Command> commands = new ArrayList<>();
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
            commands.add(new Command("show","food_list", GameUtils::showFoodList));
            commands.add(new Command("food", "rate",GameUtils::setFoodRate));
            commands.add(new Command("show", "food_rate", GameUtils::showFoodRate));
            commands.add(new Command("tax", "rate", GameUtils::taxRate));
            commands.add(new Command("show", "tax_rate", GameUtils::showTaxRate));
            commands.add(new Command("fear", "rate", GameUtils::fearRate));
            commands.add(new Command("drop", "building", GameUtils::dropBuilding));
            commands.add(new Command("select", "building", GameUtils::selectBuilding));
            commands.add(new Command("create", "unit", GameUtils::creatUnit));
            commands.add(new Command("repair", "building", GameUtils::repair));
            commands.add(new Command("exit", "game_menu",MenuUtils::enterMainMenu));

        } else if (menuName == MenuName.MAP_MENU) {
            commands.add(new Command("move", "map", GameUtils::moveMap));
            commands.add(new Command("show", "details", GameUtils::showDetails));
            commands.add((new Command("exit","map_menu", GameUtils::exitFromMapMenu)));
        } else if (menuName == MenuName.MAIN_MENU) {
            commands.add(new Command("user", "logout", MenuUtils::userLogout));
            commands.add(new Command("enter", "game_menu", MenuUtils::enterGameMenu));
            commands.add(new Command("enter", "profile_menu", MenuUtils::enterProfileMenu));

        }

        return new AppMenu(commands, scanner);
    }

    public static AppMenu getGameEntityMenu(GameEntity gameEntity, Scanner scanner){
        ArrayList<Command> commands = new ArrayList<>();

        if (gameEntity instanceof Unit){
            commands.add(new Command("unit", "move_to", GameEntityUtils::moveUnit));
            commands.add(new Command("set", "stance", GameEntityUtils::setStance));

            for (Attribute attribute:
                    gameEntity.getAttributes()) {
                if (attribute instanceof CloseCombat){
                    commands.add(new Command("attack", "melee", GameEntityUtils::attack));
                }
            }
        }

        if (gameEntity instanceof Building){
            for (Attribute attribute:
                    gameEntity.getAttributes()) {
                if (attribute instanceof CreateUnit)
                    commands.add(new Command("create", "unit", GameEntityUtils::createUnit));
                else if (attribute instanceof HasHP)
                    commands.add(new Command("repair", null, GameEntityUtils::repair));
            }
        }

        return new AppMenu(commands, scanner);
    }

    public static void show(String output) {
        System.out.println(output);
    }

    public void run() {
        AppMenu tempMenu = MainController.getCurrentMenu();
        while (tempMenu.equals(MainController.getCurrentMenu())) {
            ParsedLine parsedLine = ParsedLine.parseLine(scanner.nextLine());
            if (parsedLine == null) System.out.println("Error: Invalid command structure.\nA command should" +
                    " have the following structure: <command> [<subcommand>] [<options>]\nAn option should have the" +
                    " following form and cannot have more than one argument: (-<shortOption>|--<longOption>) [<argument>]");
            else {
                boolean isValid = false;
                for (Command command :
                        commands)
                    if (command.command.equals(parsedLine.command) && command.subcommand.equals(parsedLine.subCommand)) {
                        command.util.accept(parsedLine);
                        isValid = true;
                        break;
                    }
                if (!isValid) System.out.println("Error: Invalid command.");
            }
        }

    }
    public static String getOneLine(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

    public enum MenuName {
        LOGIN_MENU,
        SIGNUP_MENU,
        MAIN_MENU,
        PROFILE_MENU,
        GAME_MENU,
        MAP_MENU,

    }
}
