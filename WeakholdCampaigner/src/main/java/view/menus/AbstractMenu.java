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

    protected AbstractMenu(ArrayList<Command> commands, AppMenu.MenuName menuName) {
        this.menuName = menuName;
        this.commands.addAll(commands);
    }

    public enum MenuName {
        LOGIN_MENU("Login Menu"),
        SIGNUP_MENU("Signup Menu"),
        MAIN_MENU("Main Menu"),
        PROFILE_MENU("Profile Menu"),
        GAME_MENU("Game Menu"),
        MAP_MENU("Map Menu"),
        ENTITY_MENU("Entity Menu"),
        TRAD_MENU("Trad Menu"),
        SHOP_MENU("shop menu");

        public final String nameString;

        MenuName(String nameString) {
            this.nameString = nameString;
        }
    }

    protected static ArrayList<Command> getCommonCommands() {
        ArrayList<Command> commonCommands = new ArrayList<>();

        commonCommands.add(new Command("show", "commands", AbstractMenu::showCommands));
        commonCommands.add(new Command("show", "current_menu", AbstractMenu::showCurrentMenu));

        return commonCommands;
    }

    private static void showCommands(ParsedLine parsedLine) {
        for (Command command :
                MainController.getCurrentMenu().commands) {
            String subcommand = command.subcommand;
            System.out.println(command.command + " " + ((subcommand != null) ? subcommand : ""));
        }
    }

    private static void showCurrentMenu(ParsedLine parsedLine) {
        System.out.println("You are currently in " + MainController.getCurrentMenu().menuName.nameString);
    }

    public static void show(String output) {
        //System.out.println(output);
        view.GUI.AbstractMenu.showInformationAlertAndWait(output);
    }

    public void run() {
        AbstractMenu tempMenu = MainController.getCurrentMenu();
        while (tempMenu.equals(MainController.getCurrentMenu())) {
            ParsedLine parsedLine = ParsedLine.parseLine(scanner.nextLine());
            if (parsedLine == null) System.out.println("Error: Invalid command structure.\n" +
                    "A command should have the following structure: <command> [<subcommand>] [<options>]\n" +
                    "An option should have the following form and cannot have more than one argument: " +
                    "(-<shortOption>|--<longOption>) [<argument>]\n" +
                    "An argument cannot contain whitespace unless given inside quotation marks");
            else {
                boolean isValid = false;
                for (Command command :
                        commands)
                    if (command.command.equals(parsedLine.command) && (
                            (command.subcommand == null && parsedLine.subCommand == null) ||
                                    (command.subcommand != null && command.subcommand.equals(parsedLine.subCommand)))) {
                        command.util.accept(parsedLine);
                        isValid = true;
                        break;
                    }
                if (!isValid) System.out.println("Error: Invalid command.");
            }
        }
    }
}
