package view.utils;

import controller.MainController;
import controller.menu_controllers.GameMenuController;
import controller.menu_controllers.LoginMenuController;
import controller.menu_controllers.ProfileMenuController;
import controller.menu_controllers.SignupMenuController;
import view.AppMenu;
import view.ParsedLine;

import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

public class MenuUtils {
    public static boolean checkInputsAreNotNull(String ... aruments){
        for(String arg:
                aruments) {
            if(arg == null){
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
        if(!checkInputsAreNotNull(username, password, passwordConfirmation, email, nickname)){
            System.out.println("Error: This command should have the following format:\n" +
                    "user create -u <username> -p <password> [--confirmation <password confirmation>]" +
                    "--email <email> -n <nickname> [-s <slogan>]");
            return;
        }
        switch (SignupMenuController.createUser(username, password, passwordConfirmation, email, nickname, slogan)) {
            case USER_CREATED_SUCCESSFULLY:
                System.out.println("Done!");
                break;
        }
    }

    public static void userLogin(ParsedLine parsedLine) {
        String username = null, password = null;
        Boolean stayLoggedin = false;

        HashMap<String, String> options = parsedLine.options;
        for (Entry<String, String> entry :
                options.entrySet()){
            String option = entry.getKey(), argument = entry.getValue();
            switch (option){
                case"-u":
                    username = argument;
                    break;
                case "-p":
                    password = argument;
                    break;
                case "--stayloggedin":
                    if(argument == null) {
                        stayLoggedin = true;
                    }
                    else {
                        stayLoggedin =null;
                    }
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "user login -u <username> -p <password> [--stayedloggedin]");
                    return;
            }
        }
        if (stayLoggedin == null){
            System.out.println("Error: This command should have the following format:\n" +
                    "user login -u <username> -p <password> [--stayedloggedin]");
            return;
        }
        if(!checkInputsAreNotNull(username, password)) {
            System.out.println("Error: This command should have the following format:\n" +
                    "user login -u <username> -p <password> [--stayloggedin]");
            return;
        }
        switch (LoginMenuController.userLogin(username, password, stayLoggedin )) {
            case USER_LOGGED_IN_SUCCESSFULLY:
                System.out.println("User logged in successfully");
                MainController.setCurrentMenu(AppMenu.MenuName.MAIN_MENU);
        }
    }

    public static void forgotPassword(ParsedLine parsedLine){
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
        if(!checkInputsAreNotNull(username)){
            System.out.println("Error: This command should have the following format:\n" +
                    "forgot my_password -u <username>");
            return;
        }
        switch (LoginMenuController.forgotPassword(username)){
            case SECURITY_QUESTION_CONFIRMED:
                System.out.println("Your password is : TODOCOMMENT");//TODO:  fill here with User class
        }
    }

    public static void userLogout(ParsedLine parsedLine){
        System.out.println("user logged out successfully!");
        MainController.setCurrentMenu(AppMenu.MenuName.LOGIN_MENU);
    }
    public static void enterGameMenu(ParsedLine parsedLine){
        MainController.setCurrentMenu(AppMenu.MenuName.GAME_MENU);
    }
    public static void enterProfileMenu(ParsedLine parsedLine){
        MainController.setCurrentMenu(AppMenu.MenuName.PROFILE_MENU);
    }
    public static void enterSignUpMenu(ParsedLine parsedLine){
        MainController.setCurrentMenu(AppMenu.MenuName.SIGNUP_MENU);
    }
    public static void enterMainMenu(ParsedLine parsedLine){
        MainController.setCurrentMenu(AppMenu.MenuName.MAIN_MENU);
    }
    public static void enterLoginMenu(ParsedLine parsedLine){
        MainController.setCurrentMenu(AppMenu.MenuName.LOGIN_MENU);
    }
    public static void profileChange(ParsedLine parsedLine) {
        String username = null, nickname = null, email = null, slogan = null;
        HashMap<String, String> options = parsedLine.options;
        for (Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "-u":
                    username = argument;
                    if(!checkInputsAreNotNull(username)){
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile change -u <username>");
                        return;
                    }
                    switch (ProfileMenuController.changeUsername(username)){
                        case USERNAME_HAS_CHANGED:
                            System.out.println("username has changed successfully");
                    }
                    break;
                case "-n":
                    nickname = argument;
                    if(!checkInputsAreNotNull(nickname)){
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile change -n <nickname>");
                        return;
                    }
                    switch (ProfileMenuController.changeNickname(nickname)){
                        case NICKNAME_HAS_CHANGED:
                            System.out.println("nickname has changed successfully");
                    }
                    break;
                case "--password":

                    break;
                case "-e":
                    email = argument;
                    if(!checkInputsAreNotNull(email)){
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile change -e <email>");
                        return;
                    }
                    switch (ProfileMenuController.changeEmail(email)){
                        case EMAIL_HAS_CHANGED:
                            System.out.println("email has changed successfully");
                    }
                    break;
                case "-s":
                    slogan = argument;
                    if(!checkInputsAreNotNull(slogan)){
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile change -s <slogan>");
                        return;
                    }
                    switch (ProfileMenuController.changeSlogan(slogan)){
                        case SLOGAN_HAS_CHANGED:
                            System.out.println("slogan has changed successfully");
                    }
                    break;
                default:
                    System.out.println("Error: This command should have the following format:\n" +
                            "profile change [-u <username>] [-n <nickname>] "+
                            "[--password -o <old password> -n <new password>] "+
                            "[-e <email>] [-s <slogan>]");
                    return;
            }
        }
    }
    public static void profileRemove(ParsedLine parsedLine){
        HashMap<String, String> options = parsedLine.options;
        for (Entry<String, String> entry :
                options.entrySet()) {
            String option = entry.getKey(), argument = entry.getValue();
            switch (option) {
                case "--slogan":
                    if(argument != null){
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile remove --slogan");
                        return;
                    }
                    switch (ProfileMenuController.changeSlogan(argument)){
                        case SLOGAN_HAS_CHANGED:
                            System.out.println("slogan has removed");
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
            String option = entry.getKey();
            switch (option) {
                case "--highscore":
                    break;
                case "--rank":
                    break;
                case "--slogan":
                    break;
                default:
                    break;
            }
        }
    }
}
