package view;

import view.utils.GameUtils;
import view.utils.MenuUtils;

import java.util.ArrayList;
import java.util.Scanner;

public class AppMenu {
    private final ArrayList<Command> commands = new ArrayList<>();
    private final Scanner scanner;

    private AppMenu(ArrayList<Command> commands, Scanner scanner) {
        this.scanner = scanner;
        this.commands.addAll(commands);
    }

    public static AppMenu getMenu(MenuName menuName, Scanner scanner) {
        ArrayList<Command> commands = new ArrayList<>();
        if (menuName == MenuName.SIGNUP_MENU) {
            commands.add(new Command("user", "create", MenuUtils::userCreate));
        } else if (menuName == MenuName.LOGIN_MENU) {
            commands.add(new Command("user", "login", MenuUtils::userLogin));
        } else if (menuName == MenuName.PROFILE_MENU) {
            commands.add(new Command("profile", "change", MenuUtils::profileChange));
        } else if (menuName == MenuName.Game_Menu) {
            commands.add(new Command("show", "map", GameUtils::showMap));
        }

        return new AppMenu(commands, scanner);
    }

    public static void show(String output) {
        System.out.println(output);
    }

    public void run() {
        while (true) {
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

    public enum MenuName {
        LOGIN_MENU,
        SIGNUP_MENU,
        MAIN_MENU,
        PROFILE_MENU,
        Game_Menu,
    }
}
