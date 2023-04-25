package view.utils;

import controller.MainController;
import controller.menu_controllers.LoginMenuController;
import controller.menu_controllers.ProfileMenuController;
import controller.menu_controllers.SignupMenuController;
import view.AppMenu;
import view.ParsedLine;

import java.util.HashMap;
import java.util.Map.Entry;

public class MenuUtils {
    public static boolean checkInputsAreNotNull(String... arguments) {
        for (String arg :
                arguments) {
            if (arg == null) {
                return false;
            }
        }
        return true;
    }

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
                    System.out.println("Error: This command should have the following format:\n" +
                            "user create -u <username> -p <password> [--confirmation <password confirmation>]" +
                            "--email <email> [-n <nickname>] [-s <slogan>]");
                    return;
            }
        }
        if (!checkInputsAreNotNull(username, password, email, nickname)) {
            System.out.println("Error: This command should have the following format:\n" +
                    "user create -u <username> -p <password> [--confirmation <password confirmation>]" +
                    "--email <email> -n <nickname> [-s <slogan>]");
            return;
        }
        switch (SignupMenuController.createUser(username, password, passwordConfirmation, email, nickname, slogan)) {
            case USER_CREATED_SUCCESSFULLY:
                System.out.println("Done!");
                break;
            case INVALID_USERNAME:
                System.out.println("Error: Username consists of letters ,numbers and underscore");
                break;
            case OPERATION_CANCELLED:
                System.out.println("Operation cancelled");
                break;
            case TAKEN_EMAIL:
                System.out.println("Error: An account has been created with this email address");
                break;
            case INVALID_EMAIL:
                System.out.println("Error:Your email format is invalid");
                break;
            case WRONG_RANDOM_PASSWORD_REENTERED:
                System.out.println("Wrong re-entered password");
                break;
            case FEW_CHARACTERS:
                System.out.println("Your password should have at least 6 character");
                break;
            case N0_LOWERCASE_LETTER:
                System.out.println("Your password doesn't have any lowercase letter");
                break;
            case N0_UPPERCASE_LETTER:
                System.out.println("Your password doesn't have any uppercase letter");
                break;
            case N0_NUMBER:
                System.out.println("Your password doesn't have any number");
                break;
            case NO_NON_WORD_NUMBER_CHARACTER:
                System.out.println("Your password doesn't have any character");
                break;
            case WRONG_PASSWORD_CONFIRMATION:
                System.out.println("Your password confirmation is wrong!");
                break;
            case WRONG_SECURITY_QUESTION_FORMAT:
                System.out.println("Error: This command should have the following format:\n" +
                        "question pick -q <question-number> -a <answer> -c <answer confirmation>");
                break;
            case OUT_OF_BOUNDS:
                System.out.println("Error: Your chosen question number should be between 1 and 3");
                break;
            case WRONG_ANSWER_CONFIRM:
                System.out.println("Error: Your answer confirmation is wrong!");
                break;
        }
    }

    public static void userLogin(ParsedLine parsedLine) {
        String username = null, password = null;
        Boolean stayLoggedIn = false;

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
                case "--stay_logged_in":
                    if (argument == null) {
                        stayLoggedIn = true;
                    } else {
                        stayLoggedIn = null;
                    }
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "user login -u <username> -p <password> [--stay_logged_in]");
                    return;
            }
        }
        if (stayLoggedIn == null) {
            System.out.println("Error: This command should have the following format:\n" +
                    "user login -u <username> -p <password> [--stay_logged_in]");
            return;
        }
        if (!checkInputsAreNotNull(username, password)) {
            System.out.println("Error: This command should have the following format:\n" +
                    "user login -u <username> -p <password> [--stay_logged_in]");
            return;
        }
        switch (LoginMenuController.userLogin(username, password, stayLoggedIn)) {
            case USER_LOGGED_IN_SUCCESSFULLY:
                System.out.println("User logged in successfully");
                MainController.setCurrentMenu(AppMenu.MenuName.MAIN_MENU);
                break;
        }
    }

    public static void forgotPassword(ParsedLine parsedLine) {
        String username = null;
        HashMap<String, String> options = parsedLine.options;
        for (Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "-u":
                    username = argument;
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "forgot my_password -u <username>");
                    return;
            }
        }
        if (!checkInputsAreNotNull(username)) {
            System.out.println("Error: This command should have the following format:\n" +
                    "forgot my_password -u <username>");
            return;
        }
        switch (LoginMenuController.forgotPassword(username)) {
            case SECURITY_QUESTION_CONFIRMED:
                System.out.println("Your password is : TODOCOMMENT");//TODO:  fill here with User class
                break;
        }
    }

    public static void userLogout(ParsedLine parsedLine) {
        System.out.println("user logged out successfully!");
        MainController.setCurrentMenu(AppMenu.MenuName.LOGIN_MENU);
    }

    public static void enterGameMenu(ParsedLine parsedLine) {
        MainController.setCurrentMenu(AppMenu.MenuName.GAME_MENU);
        System.out.println("entered game_menu");
    }

    public static void enterProfileMenu(ParsedLine parsedLine) {
        MainController.setCurrentMenu(AppMenu.MenuName.PROFILE_MENU);
        System.out.println("enter profile_menu");
    }

    public static void enterSignUpMenu(ParsedLine parsedLine) {
        MainController.setCurrentMenu(AppMenu.MenuName.SIGNUP_MENU);
        System.out.println("entered signup_menu");
    }

    public static void enterMainMenu(ParsedLine parsedLine) {
        MainController.setCurrentMenu(AppMenu.MenuName.MAIN_MENU);
        System.out.println("entered main_menu");
    }

    public static void enterLoginMenu(ParsedLine parsedLine) {
        MainController.setCurrentMenu(AppMenu.MenuName.LOGIN_MENU);
        System.out.println("entered login_menu");
    }

    public static void profileChange(ParsedLine parsedLine) {
        String username = null, nickname = null, email = null, slogan = null, newPassword = null, oldPassword = null;
        HashMap<String, String> options = parsedLine.options;
        loop:
        for (Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "-u":
                    username = argument;
                    if (!checkInputsAreNotNull(username)) {
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile change -u <username>");
                        return;
                    }
                    switch (ProfileMenuController.changeUsername(username)) {
                        case USERNAME_HAS_CHANGED:
                            System.out.println("username has changed successfully");
                            break;
                        case INVALID_USERNAME:
                            System.out.println("Error: Username consists of letters ,numbers and underscore");
                            break;
                        case TAKEN_USERNAME:
                            System.out.println("Error: This username has already been taken");
                            break;
                    }
                    break;
                case "--password":
                    oldPassword = options.get("-o");
                    newPassword = options.get("-n");
                    if (!checkInputsAreNotNull(newPassword, oldPassword)) {
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile change -password -o <old password> -n <new password>");
                        return;
                    }
                    switch (ProfileMenuController.changePassword(oldPassword, newPassword)) {
                        case PASSWORD_HAS_CHANGED:
                            System.out.println("password has changed successfully");
                            break;
                        case FEW_CHARACTERS:
                            System.out.println("Your password should have at least 6 character");
                            break;
                        case N0_LOWERCASE_LETTER:
                            System.out.println("Your password doesn't have any lowercase letter");
                            break;
                        case N0_UPPERCASE_LETTER:
                            System.out.println("Your password doesn't have any uppercase letter");
                            break;
                        case N0_NUMBER:
                            System.out.println("Your password doesn't have any number");
                            break;
                        case NO_NON_WORD_NUMBER_CHARACTER:
                            System.out.println("Your password doesn't have any character");
                            break;
                        case INCORRECT_CURRENT_PASSWORD:
                            System.out.println("Your old password is wrong!");
                            break;
                        case SAME_PASSWORD:
                            System.out.println("Your new password is the same as the previous one");
                            break;
                        case WRONG_PASSWORD_CONFIRMATION:
                            System.out.println("Your password confirmation is wrong!");
                            break;
                    }
                    break loop;
                case "-n":
                    nickname = argument;
                    if (!checkInputsAreNotNull(nickname)) {
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile change -n <nickname>");
                        return;
                    }
                    switch (ProfileMenuController.changeNickname(nickname)) {
                        case NICKNAME_HAS_CHANGED:
                            System.out.println("nickname has changed successfully");
                            break;
                    }
                    break;
                case "-e":
                    email = argument;
                    if (!checkInputsAreNotNull(email)) {
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile change -e <email>");
                        return;
                    }
                    switch (ProfileMenuController.changeEmail(email)) {
                        case EMAIL_HAS_CHANGED:
                            System.out.println("email has changed successfully");
                            break;
                        case INVALID_EMAIL:
                            System.out.println("Error:Your email format is invalid");
                            break;
                        case TAKEN_EMAIL:
                            System.out.println("Error: An account has been created with this email address");
                            break;
                    }
                    break;
                case "-s":
                    slogan = argument;
                    if (!checkInputsAreNotNull(slogan)) {
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile change -s <slogan>");
                        return;
                    }
                    switch (ProfileMenuController.changeSlogan(slogan)) {
                        case SLOGAN_HAS_CHANGED:
                            System.out.println("slogan has changed successfully");
                            break;
                    }
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "profile change [-u <username>] [-n <nickname>] " +
                            "[--password -o <old password> -n <new password>] " +
                            "[-e <email>] [-s <slogan>]");
                    return;
            }
        }
        if (options.size() == 0)
            System.out.println("Error: This command should have the following format:\n" +
                    "profile change [-u <username>] [-n <nickname>] " +
                    "[--password --old <old password> --new <new password>] " +
                    "[-e <email>] [-s <slogan>]");

    }

    public static void profileRemove(ParsedLine parsedLine) {
        HashMap<String, String> options = parsedLine.options;
        for (Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "--slogan":
                    if (argument != null) {
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile remove --slogan");
                        return;
                    }
                    switch (ProfileMenuController.changeSlogan(argument)) {
                        case SLOGAN_HAS_CHANGED:
                            System.out.println("slogan has removed");
                            break;
                    }
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "profile remove --slogan");
                    return;
            }
        }
    }

    public static void profileDisplay(ParsedLine parsedLine) {
        HashMap<String, String> options = parsedLine.options;
        for (Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "--highscore":
                    if (checkInputsAreNotNull(argument)) {
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile display --highscore");
                        return;
                    }
                    System.out.println("The highscore is: " + ProfileMenuController.displayHighScore());
                    break;
                case "--rank":
                    if (checkInputsAreNotNull(argument)) {
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile display --rank");
                        return;
                    }
                    System.out.println("This user's rank is: " + ProfileMenuController.displayRank());
                    break;
                case "--slogan":
                    if (checkInputsAreNotNull(argument)) {
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile display --slogan");
                        return;
                    }
                    switch (ProfileMenuController.displaySlogan()) {
                        case DISPLAY:
                            System.out.println("This user's slogan is : "+ MainController.getCurrentUser().getSlogan());
                            break;
                        case NULL_SLOGAN:
                            System.out.println("Slogan is empty!");
                    }
                    break;
                case "--all":
                    if (checkInputsAreNotNull(argument)) {
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile display --all");
                        return;
                    }
                    ProfileMenuController.displayProfile();
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "profile display [--slogan | --highscore | --rank | --all]");
                    return;

            }
        }
        if (options.size() == 0)
            System.out.println("Error: This command should have the following format:\n" +
                    "profile display [--slogan | --highscore | --rank | --all]");

    }
}
