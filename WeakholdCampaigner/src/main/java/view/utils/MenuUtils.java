package view.utils;

import controller.MainController;
import controller.menu_controllers.GameMenuController;
import controller.menu_controllers.LoginMenuController;
import controller.menu_controllers.SignupMenuController;
import view.AppMenu;
import view.ParsedLine;

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
        if(!checkInputsAreNotNull(username, password, passwordConfirmation, email)){
            System.out.println("Error: This command should have the following format:\n" +
                    "user create -u <username> -p <password> [--confirmation <password confirmation>]" +
                    "--email <email> [-n <nickname>] [-s <slogan>]");
            return;
        }
        switch (SignupMenuController.createUser(username, password, passwordConfirmation, email, nickname, slogan)) {
            case USER_CREATED_SUCCESSFULLY:
                System.out.println("Done!");
                break;
        }
    }

    public static void enterLoginMenu(ParsedLine parsedLine){
        MainController.setCurrentMenu(AppMenu.MenuName.LOGIN_MENU);
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

    public static void profileChange(ParsedLine parsedLine) {

    }

}
