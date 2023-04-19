package view.utils;

import controller.menu_controllers.SignupMenuController;
import view.ParsedLine;

import java.util.HashMap;
import java.util.Map.Entry;

public class MenuUtils {
    public static void userCreate(ParsedLine parsedLine) {
        String username = null, password = null, passwordConfirmation = null, email = null, nickname = null,
                slogan = null;

        HashMap<String, String> options = parsedLine.options;
        for (Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "-u":
                    username = argument;
                    break;
                case "-p":
                    password = argument;
                    break;
                case "--confirmation":
                    passwordConfirmation = argument;
                    break;
                case "--email":
                    email = argument;
                    break;
                case "-n":
                    nickname = argument;
                    break;
                case "-s":
                    slogan = argument;
                    break;
                default:
                    System.out.println("Error: This command must have the following format: " +
                            "user create -u <username> -p <password> [--confirmation <password confirmation>]" +
                            "–email <email> [-n <nickname>] [-s <slogan>]");
                    return;
            }
        }

        switch (SignupMenuController.createUser(username, password, passwordConfirmation, email, nickname, slogan)) {
            case USER_CREATED_SUCCESSFULLY:
                System.out.println("YES");
                break;
        }
    }

    public static void userLogin(ParsedLine parsedLine) {

    }

    public static void profileChange(ParsedLine parsedLine) {

    }
}
