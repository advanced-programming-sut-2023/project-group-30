package view.menus;

import controller.MainController;
import view.Command;
import view.ParsedLine;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class AbstractMenu {
    protected final ArrayList<Command> commands = new ArrayList<>();
    protected static Scanner scanner = null;
    public final AppMenu.MenuName menuName;

    protected AbstractMenu(ArrayList<Command> commands, Scanner scanner, AppMenu.MenuName menuName) {
        this.menuName = menuName;
        this.scanner = scanner;
        this.commands.addAll(commands);
    }

    public enum MenuName {
        LOGIN_MENU("Login Menu"),
        SIGNUP_MENU("Signup Menu"),
        MAIN_MENU("Main Menu"),
        PROFILE_MENU("Profile Menu"),
        GAME_MENU("Game Menu"),
        MAP_MENU("Map Menu"),
        ENTITY_MENU("Entity Menu");

        public final String nameString;

        MenuName(String nameString) {
            this.nameString = nameString;
        }
    }

    protected static void showCommands(ParsedLine parsedLine){
        for (Command command :
                MainController.getCurrentMenu().commands) {
            String subcommand = command.subcommand;
            System.out.println(command.command + " " + ((subcommand != null) ? subcommand : ""));
        }
    }

    protected static ArrayList<Command> getCommonCommands(){
        ArrayList<Command> commonCommands = new ArrayList<>();

        commonCommands.add(new Command("show", "commands", AbstractMenu::showCommands));

        return commonCommands;
    }

    public static void show(String output) {
        System.out.println(output);
    }
}
