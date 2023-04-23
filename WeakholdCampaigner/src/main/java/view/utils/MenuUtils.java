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
        if(!checkInputsAreNotNull(username, password, email, nickname)){
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
                break;
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
                break;
        }
    }

    public static void userLogout(ParsedLine parsedLine){
        System.out.println("user logged out successfully!");
        MainController.setCurrentMenu(AppMenu.MenuName.LOGIN_MENU);
    }
    public static void enterGameMenu(ParsedLine parsedLine){
        MainController.setCurrentMenu(AppMenu.MenuName.GAME_MENU);
        System.out.println("entered game_menu");
    }
    public static void enterProfileMenu(ParsedLine parsedLine){
        MainController.setCurrentMenu(AppMenu.MenuName.PROFILE_MENU);
        System.out.println("enter profile_menu");
    }
    public static void enterSignUpMenu(ParsedLine parsedLine){
        MainController.setCurrentMenu(AppMenu.MenuName.SIGNUP_MENU);
        System.out.println("entered signup_menu");
    }
    public static void enterMainMenu(ParsedLine parsedLine){
        MainController.setCurrentMenu(AppMenu.MenuName.MAIN_MENU);
        System.out.println("entered main_menu");
    }
    public static void enterLoginMenu(ParsedLine parsedLine){
        MainController.setCurrentMenu(AppMenu.MenuName.LOGIN_MENU);
        System.out.println("entered login_menu");
    }
    public static void profileChange(ParsedLine parsedLine) {
        String username = null, nickname = null, email = null, slogan = null, newpassword = null, oldpassword = null;
        HashMap<String, String> options = parsedLine.options;
        loop: for (Entry<String, String> entry :
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
                case "--password":
                    oldpassword = options.get("-o");
                    newpassword = options.get("-n");
                    if(!checkInputsAreNotNull(newpassword, oldpassword)){
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile change -password -o <old password> -n <new password>");
                        return;
                    }
                    switch (ProfileMenuController.changePassword(oldpassword, newpassword)){
                        case PASSWORD_HAS_CHANGED:
                            System.out.println("password has changed successfully");
                            break;
                    }
                    break loop;
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
                            break;
                    }
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
                            break;
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
                            break;
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
        if (options.size() == 0)
            System.out.println("Error: This command should have the following format:\n" +
                    "profile change [-u <username>] [-n <nickname>] "+
                    "[--password --old <old password> --new <new password>] "+
                    "[-e <email>] [-s <slogan>]");

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
                    if(checkInputsAreNotNull(argument)){
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile display --highscore");
                        return;
                    }
                    System.out.println("The highscore is: " + ProfileMenuController.displayHighscore());
                    break;
                case "--rank":
                    if(checkInputsAreNotNull(argument)){
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile display --rank");
                        return;
                    }
                    System.out.println("This user's rank is: " + ProfileMenuController.displayRank());
                    break;
                case "--slogan":
                    if(checkInputsAreNotNull(argument)){
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile display --slogan");
                        return;
                    }
                    switch (ProfileMenuController.displaySlogan()){
                        case DISPLAY:
                            System.out.println("This user's slogan is : TODO");//TODO: fill here with  user class
                            break;
                    }
                    break;
                case "--all":
                    if(checkInputsAreNotNull(argument)){
                        System.out.println("Error: This command should have the following format:\n" +
                                "profile display --all");
                        return;
                    }
                    switch (ProfileMenuController.displayProfile()){
                        case DISPLAY:
                            System.out.println("PROFILE DISPLAY");//TODO: fill here with  user class
                            break;
                    }
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
